package Modelo;

import java.sql.Timestamp;

/**
 * Modelo que representa un registro de auditoría en el sistema bancario.
 * Mapea directamente los campos de la tabla LogAuditoria de la base de datos.
 * @author Alejandro Ferrándiz Martínez
 */
public class LogAuditoria {
    
    private int idLog;
    private String accion;
    private Timestamp fechaAccion;
    private boolean resultado;
    private String idUsuario;
    
  
    public LogAuditoria(int idLog, String accion, Timestamp fechaAccion, boolean resultado, String idUsuario) {
        this.idLog = idLog;
        this.accion = accion;
        this.fechaAccion = fechaAccion;
        this.resultado = resultado;
        this.idUsuario = idUsuario;
    }

    /**
     * Constructor útil para registrar nuevas acciones administratas. 
     * El idLog y la fechaAccion no se piden porque los genera automáticamente la BD.
     */
    public LogAuditoria(String accion, boolean resultado, String idUsuario) {
        this.accion = accion;
        this.resultado = resultado;
        this.idUsuario = idUsuario;
    }

    // GETTERS Y SETTERS
    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Timestamp getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(Timestamp fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public boolean isResultado() {
        return resultado;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}