package Modelo;

import java.util.Date;

public class Incidencia {
	
	private String estado;
    private Date fechaCreacion;
    private Date fechaResolucion;
    private String descripcion;
    private String idIncidencia;
    private String idCliente; 
    private String idEmpleado;
    
    public Incidencia(String estado, Date fechaCreacion, Date fechaResolucion, String descripcion, String idIncidencia, String idCliente, String idEmpleado) {
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaResolucion = fechaResolucion;
        this.descripcion = descripcion;
        this.idIncidencia = idIncidencia;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
    }
    
    public void resolverIncidencia() {
    	// Logica
    }
    
    public boolean esResuelta() {
    	return false; 
    }

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdIncidencia() {
		return idIncidencia;
	}

	public void setIdIncidencia(String idIncidencia) {
		this.idIncidencia = idIncidencia;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
    
    

}
