package Modelo;

public class Retirada extends Operacion {
	
    public Retirada(double importe, String idCuentaOrigen) {
        super(importe, idCuentaOrigen, "CAJERO", "COMPLETADA", idCuentaOrigen, null);
    }
    
    
    @Override 
    public void generarFechaOperacion() {
    	
    }
    
    @Override 
    public void generarCodigoOperacion() {
    	
    }
}