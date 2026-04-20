package Modelo;

public class Bizum extends Operacion {
	
	private String telefono;
	private static final double LIMITE_DIARIO = 750.0;


	public Bizum(double importe, String autorOperacion, String beneficiario, String estadoOperacion, String idCuentaOrigen, String idCuentaDestino, String telefono) {
        super(importe, autorOperacion, beneficiario, estadoOperacion, idCuentaOrigen, idCuentaDestino);
        this.telefono = telefono;
    }

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	

	public static double getLimiteDiario() {
		return LIMITE_DIARIO;
	}
	

	@Override
	public void generarFechaOperacion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generarCodigoOperacion() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
