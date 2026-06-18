package Excepciones.Sistema;

import java.sql.SQLException;
import Excepciones.BancaAppException;

public class PersistenciaException extends BancaAppException {
    
    private static final long serialVersionUID = 1L;

 
    public PersistenciaException(String mensaje, String codigo) {
        super(mensaje, codigo);
    }

    
    public PersistenciaException(String mensaje) {
        super(mensaje, "ERR_BASE_DATOS");
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, "ERR_BASE_DATOS", causa);
    }

    public PersistenciaException(String mensaje, SQLException causa) {
        super(mensaje, "ERR_BASE_DATOS", causa);
    }
}