package Excepciones.Cuenta;

import Excepciones.BancaAppException;

public class CuentaInactivaException extends BancaAppException {
   
	private static final long serialVersionUID = 1L;

	public CuentaInactivaException(String mensaje) {
        super(mensaje, "ERR_CUENTA_INACTIVA");
    }
}