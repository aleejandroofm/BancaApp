package Modelo;

import java.util.Date;

public abstract class Cuenta {
	
	private String titular;
    private String numeroCuenta;
    private Double saldo;
    private Double interes;
    private Date fechaRegistro;
    private boolean estadoCuenta;

    // 2. Constructor
    public Cuenta(String titular, String numeroCuenta, Double saldo, Double interes, Date fechaRegistro, boolean estadoCuenta) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.interes = interes;
        this.fechaRegistro = fechaRegistro;
        this.estadoCuenta = estadoCuenta;
    }

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public boolean isEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(boolean estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
    
	public abstract boolean ingresarDinero(double importe);

    public abstract boolean retirarDinero(double importe);

    public abstract boolean realizarTransferencia(double importe);

	

}
