package Excepciones.Autenticacion;

import Excepciones.BancaAppException;

public class CredencialesInvalidasException extends BancaAppException {
    
	private static final long serialVersionUID = 1L;

	public CredencialesInvalidasException(String mensaje) {
        super(mensaje, "ERR_AUTENTICACION");
    }
}