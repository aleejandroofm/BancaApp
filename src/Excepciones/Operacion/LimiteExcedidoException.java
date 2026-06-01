package Excepciones.Operacion;

import Excepciones.BancaAppException;

public class LimiteExcedidoException extends BancaAppException {
    
	private static final long serialVersionUID = 1L;

	public LimiteExcedidoException(String mensaje) {
        super(mensaje, "ERR_LIMITE_EXCEDIDO");
    }
}