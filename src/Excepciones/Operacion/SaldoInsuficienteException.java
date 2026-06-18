package Excepciones.Operacion;

import Excepciones.BancaAppException;

public class SaldoInsuficienteException extends BancaAppException {
    
    private static final long serialVersionUID = 1L;

    public SaldoInsuficienteException(String mensaje, String codigo) {
        super(mensaje, codigo);
    }

    public SaldoInsuficienteException(String mensaje) {
        super(mensaje, "ERR_SALDO_INSUFICIENTE");
    }
}