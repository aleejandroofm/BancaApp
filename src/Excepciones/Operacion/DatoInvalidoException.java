package Excepciones.Operacion;

import Excepciones.BancaAppException;

public class DatoInvalidoException extends BancaAppException {
 
	private static final long serialVersionUID = 1L;

	public DatoInvalidoException(String mensaje) {
        super(mensaje, "ERR_DATO_INVALIDO");
    }
}