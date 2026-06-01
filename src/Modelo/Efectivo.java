package Modelo;


public class Efectivo extends Operacion {
	

	public Efectivo(double importe, String autorOperacion, String beneficiario, String estadoOperacion, String idCuentaOrigen, String idCuentaDestino) {
        super(importe, autorOperacion, beneficiario, estadoOperacion, idCuentaOrigen, idCuentaDestino);
    }
	
	public void calcularCambio(double entregado) {
		double cambio = entregado - getImporte();
		System.out.println("El cambio a devolver es: " + cambio + " €");
	}

	@Override
	public void generarFechaOperacion() {
		
	}

	@Override
	public void generarCodigoOperacion() {
		
	}
}