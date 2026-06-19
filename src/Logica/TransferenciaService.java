package Logica;

import java.sql.Connection;
import java.sql.SQLException;
import Conexion.ConexionDB;
import Excepciones.Cuenta.CuentaInactivaException;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Dao.OperacionDAO;
import Modelo.Transferencia;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Excepciones.Sistema.PersistenciaException;
import Validaciones.ValidadorIBAN;
import Validaciones.VerificadorCuenta;
import Dao.CuentaDAO;
import Modelo.Cuenta;
import Modelo.TipoOperacion;

/**
 * Servicio encargado de gestionar la lógica de las transferencias bancarias.
 * @author Alejandro Ferrándiz Martínez
 */
public class TransferenciaService {
    
    private CuentaDAO dao = new CuentaDAO();
    private OperacionDAO operacionDao = new OperacionDAO();
    private VerificadorCuenta verify = new VerificadorCuenta();
    
    /**
     * Realiza una transferencia transaccional entre dos cuentas bancarias.
     * Valida el formato del IBAN, que las cuentas sean distintas, el saldo disponible
     * y que ninguna de las dos cuentas implicadas esté bloqueada en el sistema.
     * @param dniOrigen DNI del titular emisor.
     * @param cuentaDestino IBAN de la cuenta receptora.
     * @param importe Cantidad de dinero a enviar.
     * @throws CuentaNoEncontradaException Si la cuenta de origen o destino no existen en la BD.
     * @throws DatoInvalidoException  Si el importe es menor o igual a cero.
     * @throws CuentaInactivaException  Si la cuenta de origen o la de destino están bloqueadas.
     * @throws SaldoInsuficienteException  Si el emisor no dispone de fondos suficientes.
     * @throws PersistenciaException Si ocurre un fallo en la base de datos durante la transacción.
     */
    public void realizarTransferencia(String dniOrigen, String cuentaDestino, double importe) 
            throws CuentaNoEncontradaException, DatoInvalidoException, CuentaInactivaException, SaldoInsuficienteException, PersistenciaException {
        
        String cuentaOrigen = dao.buscarNumeroCuentaPorDni(dniOrigen);
        if (cuentaOrigen == null) {
            throw new CuentaNoEncontradaException("No se ha encontrado la cuenta origen asociada al cliente.", "ERR-TX-404");
        }
    
        ValidadorIBAN.validarFormato(cuentaDestino);
        ValidadorIBAN.validarCuentasDistintas(cuentaOrigen, cuentaDestino);
        
        if (importe <= 0) {
            throw new DatoInvalidoException("El importe de la transferencia debe ser mayor que 0 €.");
        }
        
        Cuenta cDestino = dao.buscarPorNumero(cuentaDestino);
        if (cDestino == null) {
            throw new CuentaNoEncontradaException("La cuenta de destino no existe.", "ERR-TX-404");
        }
        
        if (!cDestino.isEstadoCuenta()) {
            throw new CuentaInactivaException("Operación denegada: La cuenta destino se encuentra bloqueada temporalmente.");
        }
        
        // 2. Buscamos la cuenta origen en la base de datos
        Cuenta cOrigen = dao.buscarPorNumero(cuentaOrigen);
        if (cOrigen == null) {
            throw new CuentaNoEncontradaException("No se han podido recuperar los datos de la cuenta origen.", "ERR-TX-404");
        }
        
        if (!cOrigen.isEstadoCuenta()) {
            throw new CuentaInactivaException("Operación denegada: Tu cuenta está bloqueada y no puede emitir transferencias.");
        }
        
        verify.verificarSaldo(cOrigen.getSaldo(), importe);
        
        double nuevoSaldoOrigen = cOrigen.getSaldo() - importe;
        double nuevoSaldoDestino = cDestino.getSaldo() + importe;
        
        Connection con = null;
        try {
            con = ConexionDB.conectar();
            con.setAutoCommit(false); 

            dao.actualizarSaldoConTransaccion(con, cuentaOrigen, nuevoSaldoOrigen);
            dao.actualizarSaldoConTransaccion(con, cDestino.getNumeroCuenta(), nuevoSaldoDestino);
            
            con.commit(); 

            Transferencia nuevaTransferencia = new Transferencia(importe, cuentaOrigen, cuentaDestino, "COMPLETADA", cuentaOrigen, cuentaDestino, cuentaDestino);
            operacionDao.registrarOperacion(nuevaTransferencia, TipoOperacion.TRANSFERENCIA);

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback(); 
                } catch (SQLException ex) {
                    throw new PersistenciaException("Fallo crítico al restaurar estado financiero.", ex);
                }
            }
            if (e instanceof CuentaNoEncontradaException) throw (CuentaNoEncontradaException) e;
            if (e instanceof DatoInvalidoException) throw (DatoInvalidoException) e;
            if (e instanceof CuentaInactivaException) throw (CuentaInactivaException) e;
            if (e instanceof SaldoInsuficienteException) throw (SaldoInsuficienteException) e;
            
            throw new PersistenciaException(e.getMessage(), e);
            
        } finally {
            if (con != null) {
                try { 
                    con.close(); 
                } catch (SQLException e) {
                    
                }
            }
        }
    }
    
    public void programarTransferencia() {}
    public void obtenerHistorial() {}
    public void generarComprobante() {}
}
