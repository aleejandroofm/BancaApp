package Modelo;

import java.util.Date;

public class VerificacionIdentidad {
	
	private String idVerif;
    private String tipoDocumento;
    private boolean verificada;
    private Date fecha;
    private String idEmpleado; 
    private String idCliente;
    
    public VerificacionIdentidad(String idVerif, String tipoDocumento, boolean verificada, Date fecha, String idEmpleado, String idCliente) {
        this.idVerif = idVerif;
        this.tipoDocumento = tipoDocumento;
        this.verificada = verificada;
        this.fecha = fecha;
        this.idEmpleado = idEmpleado;
        this.idCliente = idCliente;
    }

	public String getIdVerif() {
		return idVerif;
	}

	public void setIdVerif(String idVerif) {
		this.idVerif = idVerif;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public boolean isVerificada() {
		return verificada;
	}

	public void setVerificada(boolean verificada) {
		this.verificada = verificada;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
    
    
}
