package Validaciones;

import Dao.CuentaDAO;
import Excepciones.Cuenta.CuentaInactivaException;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.LimiteExcedidoException;
import Excepciones.Operacion.SaldoInsuficienteException;

/**
 * Clase que se encarga de centralizar todas las validaciones de las cuentas.
 * Comprueba que el saldo sea suficiente, que las cuentas estén activas, 
 * los límites de dinero de los Bizum y los formatos de los teléfonos.
 */
public class VerificadorCuenta {
    
    private CuentaDAO dao;
    
    /**
     * Constructor que inicializa el CuentaDAO para poder hacer comprobaciones
     * directamente con la base de datos cuando haga falta.
     */
    public VerificadorCuenta() {
        this.dao = new CuentaDAO();
    }
    
    /**
     * Comprueba si el usuario tiene dinero suficiente en la cuenta para la operación.
     * @param saldoActual El dinero que hay ahora mismo en la cuenta.
     * @param cantidadARetirar El dinero que se pretende sacar o transferir.
     * @throws SaldoInsuficienteException Si el saldo actual es menor que lo que se quiere retirar.
     */
    public void verificarSaldo(Double saldoActual, Double cantidadARetirar) {
        if (saldoActual < cantidadARetirar) {
            throw new SaldoInsuficienteException("El saldo es insuficiente para realizar la operación.");
        }
    }
    
    /**
     * Comprueba si la cuenta está de alta y operativa para trabajar.
     * @param activa True si la cuenta está abierta y funcionando, false si está bloqueada.
     * @throws CuentaInactivaException Si el estado de la cuenta es false.
     */
    public void verificarEstado(boolean activa) {
        if (!activa) {
            throw new CuentaInactivaException("La cuenta no está activa o se encuentra bloqueada.");
        }
    }
    
    /**
     * Controla las restricciones de dinero específicas para la operativa de Bizum.
     * Valida que no supere el máximo legal de 750 € ni se quede por debajo de los 50 céntimos.
     * @param importe La cantidad de dinero que se quiere enviar por Bizum.
     * @throws LimiteExcedidoException Si el importe pasa de 750 €.
     * @throws DatoInvalidoException Si el importe es menor de 0.50 €.
     */
    public void verificarBizum(double importe) {
        if (importe > 750.00) {
            throw new LimiteExcedidoException("El importe máximo permitido para operaciones Bizum es de 750 €.");
        }
        
        if (importe < 0.50) {
            throw new DatoInvalidoException("El importe mínimo para realizar un Bizum es de 0.50 €.");
        }
    }
    
    /**
     * Valida que un número de teléfono sea correcto y que esté registrado.
     * Comprueba que tenga 9 dígitos y busca en el DAO si está asociado a alguna cuenta bancaria.
     * @param telefono El número de teléfono que introduce el usuario.
     * @throws DatoInvalidoException Si el texto es nulo o no tiene exactamente 9 caracteres.
     * @throws CuentaNoEncontradaException Si el teléfono no tiene ninguna cuenta asociada en la BD.
     */
    public void verificarTelefono(String telefono) {
        
        if (telefono == null || telefono.trim().length() != 9) {
            throw new DatoInvalidoException("El número de teléfono introducido debe tener exactamente 9 dígitos.");
        }
        
        String iban = dao.obtenerIbanPorTelefono(telefono);
        
        if (iban == null) {
            throw new CuentaNoEncontradaException("Este teléfono no está registrado en el sistema de Bizum.");
        }
    }
}