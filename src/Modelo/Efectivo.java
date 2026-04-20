package Modelo;

public class Efectivo extends Operacion {
	
	private Double importe;
	
	public Efectivo(double importeBase, String autorOperacion, String beneficiario, String estadoOperacion, String idCuentaOrigen, String idCuentaDestino, Double importeEfectivo) {
        super(importeBase, autorOperacion, beneficiario, estadoOperacion, idCuentaOrigen, idCuentaDestino);
        this.importe = importeEfectivo;
    }

	public double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}
	
	public void calcularCambio(Double importe, Double entregado) {
		// Logica
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
