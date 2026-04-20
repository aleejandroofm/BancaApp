package Modelo;

import java.util.Date;

public class CuentaAhorro extends Cuenta {
	
	private double tasaInteresAhorro;
	
	public CuentaAhorro(String titular, String numeroCuenta, Double saldo, Double interes, Date fechaRegistro, boolean estadoCuenta, double tasaInteresAhorro) {
        super(titular, numeroCuenta, saldo, interes, fechaRegistro, estadoCuenta);
        this.tasaInteresAhorro = tasaInteresAhorro;
    }
	
	public void aplicarInteresAhorro() {
		// Lógica
	}
	
	
    public void calcularInteresAhorro() {
    	// Lógica
    }

	public double getTasaInteresAhorro() {
		return tasaInteresAhorro;
	}

	public void setTasaInteresAhorro(double tasaInteresAhorro) {
		this.tasaInteresAhorro = tasaInteresAhorro;
	}


	@Override
	public boolean ingresarDinero(double importe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean realizarTransferencia(double importe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retirarDinero(double importe) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
