package Excepciones.Operacion;

import Excepciones.BancaAppException;

public class DestinatarioInvalidoException extends BancaAppException {
	
    private static final long serialVersionUID = 1L;

    public DestinatarioInvalidoException(String mensaje) {
        super(mensaje, "ERR-OPERACION-400");
    }
}