package Logica;

import Dao.CuentaDAO;
import Dao.OperacionDAO;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Cuenta.CuentaInactivaException;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Excepciones.Operacion.LimiteExcedidoException;
import Excepciones.Operacion.DestinatarioInvalidoException;
import Excepciones.Sistema.PersistenciaException;
import Modelo.Cuenta;
import Modelo.Retirada;
import Modelo.Ingreso;
import Modelo.Bizum;
import Modelo.TipoOperacion;

/**
 * Servicio encargado de gestionar la lógica de negocio y movimientos de las cuentas.
 * Garantiza la consistencia atómica de los saldos y sus registros de operaciones.
 * @author Alejandro Ferrándiz Martínez
 */
public class CuentaService {
    
    private CuentaDAO cuentaDao;
    private OperacionDAO operacionDao;
    
    public CuentaService() {
        this.cuentaDao = new CuentaDAO();
        this.operacionDao = new OperacionDAO();
    }
    
    /**
     * Procesa la retirada de efectivo de forma segura. Validando las reglas antes de modificar saldos.
     * @param numeroCuenta Código IBAN de la cuenta corriente.
     * @param cantidad Importe neto a extraer.
     * @throws DatoInvalidoException Si la cantidad es menor o igual a cero o nula.
     * @throws CuentaNoEncontradaException Si la cuenta no existe en la BD.
     * @throws CuentaInactivaException Si la cuenta se encuentra bloqueada en MySQL.
     * @throws SaldoInsuficienteException Si el importe supera el saldo actual.
     * @throws PersistenciaException Si ocurre un error de escritura en el sistema relacional.
     */
    public void retirarDinero(String numeroCuenta, Double cantidad) throws DatoInvalidoException, CuentaNoEncontradaException, CuentaInactivaException, SaldoInsuficienteException, PersistenciaException {

        if (cantidad == null || cantidad <= 0) {
            throw new DatoInvalidoException("El importe a retirar debe ser mayor que 0 €.");
        }

        Cuenta cuenta = cuentaDao.buscarPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("La cuenta especificada no existe.", "ERR-CUENTA-404");
        }
        
        if (!cuenta.isEstadoCuenta()) {
            throw new CuentaInactivaException("No se puede retirar dinero de una cuenta bloqueada o inactiva.");
        }
        
        if (cuenta.getSaldo() < cantidad) {
            throw new SaldoInsuficienteException("El saldo es insuficiente para realizar la operación.");
        }

        Retirada opRetirada = new Retirada(cantidad, numeroCuenta);
        
        try {
            operacionDao.registrarOperacion(opRetirada, TipoOperacion.EFECTIVO);
            
            double nuevoSaldo = cuenta.getSaldo() - cantidad;
            cuentaDao.actualizarSaldo(numeroCuenta, nuevoSaldo);
            
        } catch (PersistenciaException e) {
            throw new PersistenciaException("Retirada cancelada por seguridad: No se pudo generar la traza transaccional.", e);
        }
    }
    
    /**
     * Procesa el ingreso de dinero en efectivo en una cuenta activa.
     * @param numeroCuenta Código IBAN de la cuenta.
     * @param cantidad Importe neto a depositar.
     * @throws DatoInvalidoException Si la cantidad introducida es menor o igual a cero.
     * @throws CuentaNoEncontradaException Si la cuenta no existe.
     * @throws CuentaInactivaException Si la cuenta está deshabilitada.
     * @throws PersistenciaException Si fallan los movimientos relacionados con las transacciones en MySQL.
     */
    public void ingresarDinero(String numeroCuenta, double cantidad) 
            throws DatoInvalidoException, CuentaNoEncontradaException, CuentaInactivaException, PersistenciaException {

        if (cantidad <= 0) {
            throw new DatoInvalidoException("El importe a ingresar debe ser mayor que 0 €.");
        }

        Cuenta cuenta = cuentaDao.buscarPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("La cuenta especificada no existe.", "ERR-CUENTA-404");
        }
        
        if (!cuenta.isEstadoCuenta()) {
            throw new CuentaInactivaException("No se permiten ingresos en cuentas bloqueadas administrativamente.");
        }

        Ingreso opIngreso = new Ingreso(cantidad, numeroCuenta);
        
        try {
            operacionDao.registrarOperacion(opIngreso, TipoOperacion.EFECTIVO);
            
            double nuevoSaldo = cuenta.getSaldo() + cantidad;
            cuentaDao.actualizarSaldo(numeroCuenta, nuevoSaldo);
            
        } catch (PersistenciaException e) {
            throw new PersistenciaException("Ingreso rechazado por el sistema: Fallo en el registro histórico.", e);
        }
    }
    
    /**
     * Ejecuta el envío de dinero mediante Bizum aplicando límites específicos de su operativa
     * y validando que ninguna de las cuentas implicadas esté bloqueada.
     * @param telefonoOrigen   Teléfono del cliente emisor.
     * @param telefonoDestino Teléfono del cliente receptor.
     * @param importe Cantidad de dinero a enviar.
     * @throws DestinatarioInvalidoException Si el origen y el destino son el mismo número.
     * @throws CuentaNoEncontradaException Si alguno de los teléfonos no tiene cuenta.
     * @throws LimiteExcedidoException Si el importe supera el máximo permitido para Bizum.
     * @throws DatoInvalidoException Si el importe es negativo o cero.
     * @throws CuentaInactivaException Si la cuenta de origen o la de destino están bloqueadas.
     * @throws SaldoInsuficienteException Si la cuenta de origen no tiene capital.
     * @throws PersistenciaException Si ocurre un error relacional en MySQL.
     */
    public void realizarBizum(String telefonoOrigen, String telefonoDestino, double importe) 
            throws DestinatarioInvalidoException, CuentaNoEncontradaException, LimiteExcedidoException, DatoInvalidoException, CuentaInactivaException, SaldoInsuficienteException, PersistenciaException {
        
        if (telefonoOrigen != null && telefonoOrigen.trim().equals(telefonoDestino.trim())) {
            throw new DestinatarioInvalidoException("Operación denegada: No puedes realizar un Bizum a tu propio número de teléfono.");
        }

        if (importe > 750.0) {
            throw new LimiteExcedidoException("No se pueden enviar más de 750.00 € por operación utilizando Bizum.");
        }

        if (importe <= 0) {
            throw new DatoInvalidoException("El importe del Bizum debe ser mayor que 0 €.");
        }
        
        String ibanOrigen = cuentaDao.obtenerIbanPorTelefono(telefonoOrigen);
        String ibanDestino = cuentaDao.obtenerIbanPorTelefono(telefonoDestino);

        if (ibanOrigen != null && ibanOrigen.trim().equalsIgnoreCase(ibanDestino.trim())) {
            throw new DestinatarioInvalidoException("Operación inválida: Ambas líneas telefónicas están vinculadas a la misma cuenta bancaria.");
        }

        if (ibanOrigen == null || ibanDestino == null) {
            throw new CuentaNoEncontradaException("No se encontró ninguna cuenta vinculada a los teléfonos facilitados.", "ERR-BIZUM-404");
        }
        
        Cuenta cuentaOrigen = cuentaDao.buscarPorNumero(ibanOrigen);
        if (cuentaOrigen == null) {
            throw new CuentaNoEncontradaException("La cuenta del emisor no es válida.", "ERR-BIZUM-404");
        }
        
        if (!cuentaOrigen.isEstadoCuenta()) {
            throw new CuentaInactivaException("Operación denegada: Tu cuenta está bloqueada y no puede emitir Bizum.");
        }
        
        if (cuentaOrigen.getSaldo() < importe) {
            throw new SaldoInsuficienteException("No dispones de saldo suficiente para emitir este Bizum.");
        }
        
        Cuenta cuentaDestino = cuentaDao.buscarPorNumero(ibanDestino);
        if (cuentaDestino == null) {
            throw new CuentaNoEncontradaException("La cuenta del destinatario no es válida.", "ERR-BIZUM-404");
        }
        
        if (!cuentaDestino.isEstadoCuenta()) {
            throw new CuentaInactivaException("Operación denegada: La cuenta del destinatario se encuentra bloqueada temporalmente.");
        }

        Bizum nuevaOperacion = new Bizum(importe, ibanOrigen, ibanDestino, "COMPLETADA", ibanOrigen, ibanDestino, telefonoDestino);

        try {
            operacionDao.registrarOperacion(nuevaOperacion, TipoOperacion.BIZUM);
            
            double nuevoSaldoOrigen = cuentaOrigen.getSaldo() - importe;
            double nuevoSaldoDestino = cuentaDestino.getSaldo() + importe;

            cuentaDao.actualizarSaldo(ibanOrigen, nuevoSaldoOrigen); 
            cuentaDao.actualizarSaldo(ibanDestino, nuevoSaldoDestino);  
            
        } catch (PersistenciaException e) {
            throw new PersistenciaException("Bizum cancelado. No se pudo registrar de forma segura en la base de datos.", e);
        }
    }
}