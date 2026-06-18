package Excepciones.Sistema;

import Excepciones.BancaAppException;

public class ConfiguracionException extends BancaAppException {
    private static final long serialVersionUID = 1L;

    public ConfiguracionException(String mensaje) {
        super(mensaje, "ERR-SYS-503");
    }

    public ConfiguracionException(String mensaje, Throwable causa) {
        super(mensaje, "ERR-SYS-503", causa);
    }
}