package Modelo;

import java.util.Date;

public class HistorialSesion {
	
	private String id;
    private Date fechaInicio;
    private Date fechaFin;
    private String ip;
    private String dispositivo;
    private String tipoAuth; 
    private String idUsuario;
    
    public HistorialSesion(String id, Date fechaInicio, Date fechaFin, String ip, String dispositivo, String tipoAuth, String idUsuario) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ip = ip;
        this.dispositivo = dispositivo;
        this.tipoAuth = tipoAuth;
        this.idUsuario = idUsuario;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getTipoAuth() {
		return tipoAuth;
	}

	public void setTipoAuth(String tipoAuth) {
		this.tipoAuth = tipoAuth;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
    
    

}
