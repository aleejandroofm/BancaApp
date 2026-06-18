package Excepciones.Cuenta;

import Excepciones.BancaAppException;

public class CuentaNoEncontradaException extends BancaAppException {
    
    private static final long serialVersionUID = 1L;

    public CuentaNoEncontradaException(String mensaje, String codigo) {
        super(mensaje, codigo);
    }

    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje, "ERR_CUENTA_NOT_FOUND");
    }
}