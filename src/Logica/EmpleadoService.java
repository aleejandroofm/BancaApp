package Logica;

import Dao.UsuarioDAO;
import Dao.CuentaDAO;
import Dao.LogAuditoriaDAO;
import Modelo.LogAuditoria;
import Modelo.Usuario;
import Excepciones.Sistema.PersistenciaException;
import Excepciones.Cuenta.CuentaNoEncontradaException;

public class EmpleadoService {

    private UsuarioDAO usuarioDao = new UsuarioDAO();
    private CuentaDAO cuentaDao = new CuentaDAO();
    private LogAuditoriaDAO logAuditoriaDao = new LogAuditoriaDAO();

    /**
     * Registra un nuevo cliente en el sistema y guarda de forma obligatoria la traza de auditoría,
     * reflejando si la operación se completó con éxito o falló.
     * La auditoría se ejecuta siempre en el bloque finally para garantizar el registro de la acción administrativa.
     * @param nuevoUsuario objeto que contiene toda la información del cliente que se va a dar de alta.
     * @param dniEmpleado DNI del empleado que realiza y autoriza la gestión desde su terminal.
     * @throws PersistenciaException si ocurre un fallo de infraestructura o restricción de integridad en la base de datos.
     */
    public void registrarNuevoCliente(Usuario nuevoUsuario, String dniEmpleado) throws PersistenciaException {
        boolean resultadoExitoso = false;
        try {
            usuarioDao.guardar(nuevoUsuario);
            resultadoExitoso = true; // Si no salta excepción, el resultado es true
        } catch (PersistenciaException e) {
            throw e;
        } finally {
            LogAuditoria log = new LogAuditoria("ALTA_USUARIO", resultadoExitoso, dniEmpleado);
            logAuditoriaDao.registrarAccion(log);
        }
    }

    /**
     * Modifica el estado operativo de una cuenta bancaria (activación o bloqueo de fondos) y
     * genera la correspondiente traza de auditoría con el resultado de la acción.
     * @param numeroCuenta código IBAN de la cuenta corriente que se desea modificar.
     * @param nuevoEstado boolean que determina el nuevo estado (true para HABILITAR_CUENTA, false para BLOQUEAR_CUENTA).
     * @param dniEmpleado DNI del empleado responsable que ejecuta el cambio de estado en ventanilla.
     * @throws CuentaNoEncontradaException si el IBAN facilitado no corresponde a ninguna cuenta registrada.
     * @throws PersistenciaException si se produce un error crítico de conexión o escritura en el servidor relacional.
     */
    public void cambiarEstadoCuenta(String numeroCuenta, boolean nuevoEstado, String dniEmpleado) throws CuentaNoEncontradaException, PersistenciaException {
        
        boolean resultadoExitoso = false;
        try {
            cuentaDao.actualizarEstadoCuenta(numeroCuenta, nuevoEstado);
            resultadoExitoso = true;
        } catch (PersistenciaException e) {
            throw e;
        } finally {
        	
            String operacion = nuevoEstado ? "HABILITAR_CUENTA" : "BLOQUEAR_CUENTA";
            LogAuditoria log = new LogAuditoria(operacion, resultadoExitoso, dniEmpleado);
            logAuditoriaDao.registrarAccion(log);
        }
    }
}
