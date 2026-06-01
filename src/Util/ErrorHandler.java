package Util;

import Excepciones.BancaAppException;

public class ErrorHandler {

    
    public interface ErrorDisplay {
        void mostrarError(String codigo, String mensaje);
    }

    /**
     * Recibe el fallo general (Exception) y la vista activa (ErrorDisplay).
     */
    public static void gestionar(Exception e, ErrorDisplay display) {
        
        if (e instanceof BancaAppException) {
            BancaAppException ex = (BancaAppException) e;
            display.mostrarError(ex.getCodigoError(), ex.getMessage());
            
        } else {
            display.mostrarError("ERR_APLICACIÓN", "Ha ocurrido un error inesperado: " + e.getMessage());
        }
    }
}
