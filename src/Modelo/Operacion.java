package Modelo;

public abstract class Operacion {
	
	private double importe;
    private String autorOperacion;
    private String beneficiario;
    private String estadoOperacion;
    private String idCuentaOrigen; // FK
    private String idCuentaDestino; // FK
    
    public Operacion(double importe, String autorOperacion, String beneficiario, String estadoOperacion, String idCuentaOrigen, String idCuentaDestino) {
        this.importe = importe;
        this.autorOperacion = autorOperacion;
        this.beneficiario = beneficiario;
        this.estadoOperacion = estadoOperacion;
        this.idCuentaOrigen = idCuentaOrigen;
        this.idCuentaDestino = idCuentaDestino;
    }
    
    public abstract void generarFechaOperacion();

    public abstract void generarCodigoOperacion();

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getAutorOperacion() {
		return autorOperacion;
	}

	public void setAutorOperacion(String autorOperacion) {
		this.autorOperacion = autorOperacion;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getEstadoOperacion() {
		return estadoOperacion;
	}

	public void setEstadoOperacion(String estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}

	public String getIdCuentaOrigen() {
		return idCuentaOrigen;
	}

	public void setIdCuentaOrigen(String idCuentaOrigen) {
		this.idCuentaOrigen = idCuentaOrigen;
	}

	public String getIdCuentaDestino() {
		return idCuentaDestino;
	}

	public void setIdCuentaDestino(String idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}
    
    

}
