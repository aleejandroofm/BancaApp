package Logica;


import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Validaciones.ValidadorIBAN;
import Dao.CuentaDAO;
import Modelo.Cuenta;

/**
 * Clase de servicio encargada de gestionar las transferencias bancarias entre cuentas.
 * Se encarga de validar los datos del emisor y receptor, comprobar que haya dinero
 * suficiente y actualizar los saldos correspondientes en la base de datos.
 */
public class TransferenciaService {
	
	private CuentaDAO dao = new CuentaDAO();
	
	/**
	 * Realiza el proceso completo de una transferencia de dinero entre dos cuentas.
	 * Busca la cuenta de origen por el DNI, valida el formato del IBAN de destino,
	 * comprueba los saldos y modifica los datos de ambas cuentas en la base de datos.
	 * @param dniOrigen El DNI del cliente que realiza y paga la transferencia.
	 * @param cuentaDestino El número de cuenta (IBAN) que va a recibir el dinero.
	 * @param importe La cantidad de dinero en euros que se va a enviar.
	 * @throws CuentaNoEncontradaException Si el DNI no tiene una cuenta asociada o el IBAN de destino no existe.
	 * @throws DatoInvalidoException Si el importe es igual o menor a 0 €.
	 * @throws SaldoInsuficienteException Si el cliente no tiene dinero suficiente para cubrir el importe.
	 */
	public void realizarTransferencia(String dniOrigen, String cuentaDestino, double importe) {
		
		String cuentaOrigen = dao.buscarNumeroCuentaPorDni(dniOrigen);
		
		if (cuentaOrigen == null) {
	        throw new CuentaNoEncontradaException("No se ha encontrado la cuenta origen asociada al cliente.");
	    }
	
		ValidadorIBAN.validarFormato(cuentaDestino);
		ValidadorIBAN.validarCuentasDistintas(cuentaOrigen, cuentaDestino);
		
		if (importe <= 0) {
			throw new DatoInvalidoException("El importe de la transferencia debe ser mayor que 0 €.");
		}
		
		Cuenta cDestino = dao.buscarPorNumero(cuentaDestino);
		
		double saldoDisponible = dao.consultarSaldo(cuentaOrigen);
		
		if (saldoDisponible < importe) {
			throw new SaldoInsuficienteException("Saldo insuficiente para realizar la operacion. Saldo disponible: " + saldoDisponible + " €");
		}
		
		double nuevoSaldoOrigen = saldoDisponible - importe;
        double nuevoSaldoDestino = cDestino.getSaldo() + importe;
        
        dao.actualizarSaldo(cuentaOrigen, nuevoSaldoOrigen);
        dao.actualizarSaldo(cDestino.getNumeroCuenta(), nuevoSaldoDestino);
	}
	
	
	public void programarTransferencia() {
		
	}
	
	
	public void obtenerHistorial() {
		
	}
	
	
	public void generarComprobante() {
		
	}
}