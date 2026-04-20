package Modelo;

import java.util.Date;

public class TarjetaCredito extends Tarjeta {
	
	private double limiteCredito;
	
	public TarjetaCredito(String numeroTarjeta, int cvv, Date fechaCaducidad, boolean estadoTarjeta, double limiteCredito) {
        super(numeroTarjeta, cvv, fechaCaducidad, estadoTarjeta);
        this.limiteCredito = limiteCredito;
    }
	
	public void pagarCuota() {
		// Lógica
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	@Override
	public void bloquearTarjeta() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cambiarPIN() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void desbloquearTarjeta() {
		// TODO Auto-generated method stub
		
	}
}
