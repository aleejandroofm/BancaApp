package Modelo;

import java.util.Date;

public class Retencion {
	
	private boolean estado;
    private Date fechaRetencion;
    private double cantidad;
    private String idRetencion;
    private String idOperacion; 
    private String idCuenta;
    
    public Retencion(boolean estado, Date fechaRetencion, double cantidad, String idRetencion, String idOperacion, String idCuenta) {
        this.estado = estado;
        this.fechaRetencion = fechaRetencion;
        this.cantidad = cantidad;
        this.idRetencion = idRetencion;
        this.idOperacion = idOperacion;
        this.idCuenta = idCuenta;
    }
    
    public void actualizarEstado() {
    	// Logica
    }
    
    
    public void cancelarRetencion() {
    	// Logica
    }
    
    
    public void aprobarRetencion() {
    	// Logica
    }

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFechaRetencion() {
		return fechaRetencion;
	}

	public void setFechaRetencion(Date fechaRetencion) {
		this.fechaRetencion = fechaRetencion;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getIdRetencion() {
		return idRetencion;
	}

	public void setIdRetencion(String idRetencion) {
		this.idRetencion = idRetencion;
	}

	public String getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}
    
    
	

}


