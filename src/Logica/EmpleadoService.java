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
     * Registra un cliente y guarda la traza usando tus campos exactos.
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
     * Modifica el estado de un IBAN y audita el resultado.
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