package Logica;

import Dao.CuentaDAO;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Modelo.Cuenta;
import Validaciones.VerificadorCuenta;

/**
 * Clase de servicio que gestiona la lógica de las cuentas bancarias.
 * Se encarga de hacer de puente entre los datos del DAO y las validaciones
 * antes de modificar saldos o realizar operaciones como Bizum.
 */
public class CuentaService {
	
	private CuentaDAO dao;
	private VerificadorCuenta verify;
	
	/**
	 * Constructor por defecto. Inicializa los objetos del DAO y el verificador.
	 */
	public CuentaService() {
		this.dao = new CuentaDAO();
		this.verify = new VerificadorCuenta(); 
	}
	
	/**
	 * Gestiona la retirada de efectivo en una cuenta.
	 * Primero busca la cuenta, comprueba que esté activa y que tenga dinero suficiente,
	 * y si todo está bien, actualiza el saldo restando la cantidad.
	 * @param numeroCuenta El IBAN de la cuenta donde se va a retirar.
	 * @param cantidad Los euros que el usuario quiere sacar.
	 */
	public void retirarDinero(String numeroCuenta, Double cantidad) {
		
		Cuenta cuenta = dao.buscarPorNumero(numeroCuenta);
		verify.verificarEstado(cuenta.isEstadoCuenta());
		verify.verificarSaldo(cuenta.getSaldo(), cantidad);
		
		double nuevoSaldo = cuenta.getSaldo() - cantidad;
		dao.actualizarSaldo(numeroCuenta, nuevoSaldo);
	}
	
	/**
	 * Gestiona el ingreso de dinero en una cuenta.
	 * Busca la cuenta en la base de datos, comprueba que no esté bloqueada
	 * y le suma la cantidad al saldo actual.
	 * @param numeroCuenta El IBAN de la cuenta donde se ingresa el dinero.
	 * @param cantidad Los euros que se van a meter en la cuenta.
	 */
	public void ingresarDinero(String numeroCuenta, double cantidad) {
		
		Cuenta cuenta = dao.buscarPorNumero(numeroCuenta);
		verify.verificarEstado(cuenta.isEstadoCuenta());
		
		double nuevoSaldo = cuenta.getSaldo() + cantidad;
		dao.actualizarSaldo(numeroCuenta, nuevoSaldo);
	}
	
	/**
	 * Lógica para realizar un Bizum entre dos números de teléfono.
	 * Valida que el importe y los teléfonos sean correctos, y busca las cuentas
	 * asociadas a esos números para poder hacer el envío.
	 * @param telefonoOrigen Teléfono de la persona que envía el Bizum.
	 * @param telefonoDestino Teléfono de la persona que recibe el dinero.
	 * @param importe Cantidad de dinero a enviar.
	 * @throws CuentaNoEncontradaException Si alguno de los teléfonos no está registrado en el banco.
	 */
	public void realizarBizum(String telefonoOrigen, String telefonoDestino, double importe) {
		
		verify.verificarBizum(importe);
		verify.verificarTelefono(telefonoOrigen);
		verify.verificarTelefono(telefonoDestino);
		
		String ibanOrigen = dao.obtenerIbanPorTelefono(telefonoOrigen);
		String ibanDestino = dao.obtenerIbanPorTelefono(telefonoDestino);
		
		if (ibanOrigen == null || ibanDestino == null) {
			throw new CuentaNoEncontradaException("No se ha encontrado una cuenta vinculada a los números de teléfono provistos.");
		}
	}
}