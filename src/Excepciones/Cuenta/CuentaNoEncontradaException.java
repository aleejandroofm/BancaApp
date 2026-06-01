package Excepciones.Cuenta;

import Excepciones.BancaAppException;

public class CuentaNoEncontradaException extends BancaAppException {
    
	private static final long serialVersionUID = 1L;

	public CuentaNoEncontradaException(String mensaje) {
        super(mensaje, "ERR_CUENTA_NO_ENCONTRADA");
    }
}