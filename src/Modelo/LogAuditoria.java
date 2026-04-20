package Modelo;

import java.util.Date;

public class LogAuditoria {
	private String idLog;
    private String accion;
    private Date fechaAccion;
    private boolean resultado;
    private String idUsuario;
    
    public LogAuditoria(String idLog, String accion, Date fechaAccion, boolean resultado, String idUsuario) {
        this.idLog = idLog;
        this.accion = accion;
        this.fechaAccion = fechaAccion;
        this.resultado = resultado;
        this.idUsuario = idUsuario;
    }

	public String getIdLog() {
		return idLog;
	}

	public void setIdLog(String idLog) {
		this.idLog = idLog;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Date getFechaAccion() {
		return fechaAccion;
	}

	public void setFechaAccion(Date fechaAccion) {
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
