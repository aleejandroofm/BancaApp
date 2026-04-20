package Modelo;

import java.util.Date;

public abstract class Tarjeta {
	private String numeroTarjeta;
    private int cvv;
    private Date fechaCaducidad;
    private boolean estadoTarjeta;
    
    
	public Tarjeta(String numeroTarjeta, int cvv, Date fechaCaducidad, boolean estadoTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
		this.cvv = cvv;
		this.fechaCaducidad = fechaCaducidad;
		this.estadoTarjeta = estadoTarjeta;
	}
	
	public abstract void bloquearTarjeta();
	
	public abstract void cambiarPIN();
	
	public abstract void desbloquearTarjeta();
	
	public boolean esActiva() {
		return this.estadoTarjeta;
	}


	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}


	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}


	public int getCvv() {
		return cvv;
	}


	public void setCvv(int cvv) {
		this.cvv = cvv;
	}


	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}


	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}


	public boolean isEstadoTarjeta() {
		return estadoTarjeta;
	}


	public void setEstadoTarjeta(boolean estadoTarjeta) {
		this.estadoTarjeta = estadoTarjeta;
	}
    
    

}
