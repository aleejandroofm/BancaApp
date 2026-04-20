package Modelo;

import java.util.Date;

public class BackupRegistros {
	
	private String idBackup;
    private Date fechaCreacion;
    private String tipoBackup;
    
    public BackupRegistros(String idBackup, Date fechaCreacion, String tipoBackup) {
        this.idBackup = idBackup;
        this.fechaCreacion = fechaCreacion;
        this.tipoBackup = tipoBackup;
    }
    
    public void restaurar() {
    	// Logica
    }
    
    
    public void eliminar() {
    	// Logica
    }

	public String getIdBackup() {
		return idBackup;
	}

	public void setIdBackup(String idBackup) {
		this.idBackup = idBackup;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getTipoBackup() {
		return tipoBackup;
	}

	public void setTipoBackup(String tipoBackup) {
		this.tipoBackup = tipoBackup;
	}
    
    
}
