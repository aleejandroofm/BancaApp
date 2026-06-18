package Excepciones.Autenticacion;

import Excepciones.BancaAppException;

public class UsuarioBloqueadoException extends BancaAppException {
    private static final long serialVersionUID = 1L;

    public UsuarioBloqueadoException(String mensaje) {
        super(mensaje, "ERR-AUTH-403");
    }
}