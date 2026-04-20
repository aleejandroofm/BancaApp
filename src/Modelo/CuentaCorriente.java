package Modelo;

import java.util.Date;

public class CuentaCorriente extends Cuenta {
	
	private double limiteDescubierto;
    private double comisionMantenimiento;
    
    public CuentaCorriente(String titular, String numeroCuenta, Double saldo, Double interes, Date fechaRegistro, boolean estadoCuenta, double limiteDescubierto, double comisionMantenimiento) {
        super(titular, numeroCuenta, saldo, interes, fechaRegistro, estadoCuenta);
        this.limiteDescubierto = limiteDescubierto;
        this.comisionMantenimiento = comisionMantenimiento;
    }
    
    public void cobrarComision() {
        // Lógica
    }

    public void verificarLimiteDescubierto() {
        // Lógica
    }

	public double getLimiteDescubierto() {
		return limiteDescubierto;
	}

	public void setLimiteDescubierto(double limiteDescubierto) {
		this.limiteDescubierto = limiteDescubierto;
	}

	public double getComisionMantenimiento() {
		return comisionMantenimiento;
	}

	public void setComisionMantenimiento(double comisionMantenimiento) {
		this.comisionMantenimiento = comisionMantenimiento;
	}

	@Override
	public boolean ingresarDinero(double importe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean retirarDinero(double importe) {
		if (importe > 0 && (saldo + limiteDescubierto) >= importe) {
			saldo = saldo - importe;
			return true;
		}
		
	}

	@Override
	public boolean realizarTransferencia(double importe) {
		// TODO Auto-generated method stub
		return false;
	}

}
