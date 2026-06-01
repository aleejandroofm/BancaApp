package Modelo;

import java.util.Date;

public class CuentaCorriente extends Cuenta {
	
	private static final long serialVersionUID = 1L;
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

	

}
