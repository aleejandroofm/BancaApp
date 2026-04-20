package Modelo;

import java.util.Date;

public class TarjetaDebito extends Tarjeta {
	
	private double limiteDiario;
	
	public TarjetaDebito(String numeroTarjeta, int cvv, Date fechaCaducidad, boolean estadoTarjeta, double limiteDiario) {
        super(numeroTarjeta, cvv, fechaCaducidad, estadoTarjeta);
        this.limiteDiario = limiteDiario;
    }
	
	public void verificarLimiteDiario() {
		// Lógica
	}

    
	public double getLimiteDiario() {
		return limiteDiario;
	}

	public void setLimiteDiario(double limiteDiario) {
		this.limiteDiario = limiteDiario;
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
