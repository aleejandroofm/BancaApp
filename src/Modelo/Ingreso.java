package Modelo;

public class Ingreso extends Operacion {
	
	
    public Ingreso(double importe, String idCuentaDestino) {
        super(importe, "CAJERO", idCuentaDestino, "COMPLETADA", null, idCuentaDestino);
    }
    
    
    @Override 
    public void generarFechaOperacion() {
    	
    }
    
    @Override 
    public void generarCodigoOperacion() {
    	
    }
}