package Excepciones;

public class BancaAppException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;
	private final String codigoError;

    // Constructor básico (Mensaje + Código)
    public BancaAppException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    // Constructor con Throwable genérico (Mensaje + Código + Causa)
    public BancaAppException(String mensaje, String codigoError, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }

    // Constructor con Exception específica (Mensaje + Código + Causa)
    public BancaAppException(String mensaje, String codigoError, Exception causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}