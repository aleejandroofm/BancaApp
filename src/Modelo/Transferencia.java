package Modelo;

public class Transferencia extends Operacion {
	
	private String iban;
	
	public Transferencia(double importe, String autorOperacion, String beneficiario, String estadoOperacion, String idCuentaOrigen, String idCuentaDestino, String iban) {
        super(importe, autorOperacion, beneficiario, estadoOperacion, idCuentaOrigen, idCuentaDestino);
        this.iban = iban;
    }

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
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
