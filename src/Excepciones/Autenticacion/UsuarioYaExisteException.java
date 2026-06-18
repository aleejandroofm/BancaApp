package Excepciones.Autenticacion;

import Excepciones.BancaAppException;

public class UsuarioYaExisteException extends BancaAppException {
    private static final long serialVersionUID = 1L;

    public UsuarioYaExisteException(String mensaje) {
        // Le pasamos el mensaje y el código de error oficial a la clase madre
        super(mensaje, "ERR_USUARIO_DUPLICADO");
    }
}