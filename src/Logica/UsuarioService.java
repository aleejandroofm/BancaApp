package Logica;

import Dao.UsuarioDAO;
import java.util.List;
import Dao.CuentaDAO;
import Dao.OperacionDAO;
import Excepciones.Autenticacion.CredencialesInvalidasException;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Modelo.Efectivo;
import Modelo.Usuario;
import Modelo.Operacion;

/**
 * Clase de servicio que gestiona la sesión de los usuarios y sus operaciones principales.
 * Se encarga de controlar el estado del usuario logueado en el sistema y de coordinar
 * los ingresos, retiradas e historial consultando a los diferentes DAOs.
 */
public class UsuarioService {
	
	private UsuarioDAO usuarioDao = new UsuarioDAO();
	private CuentaDAO cuentaDao = new CuentaDAO();
	private Usuario usuarioAutenticado;
	private OperacionDAO operacionDao = new OperacionDAO();
	
	/**
	 * Comprueba el correo y la clave con la base de datos para iniciar sesión.
	 * Si el login es correcto, cambia el estado del usuario a autenticado y lo guarda
	 * en la sesión activa de memoria para el resto de gestiones.
	 * @param email Correo electrónico del usuario.
	 * @param password Contraseña en texto plano introducida.
	 * @return El objeto Usuario correspondiente que ha iniciado sesión.
	 * @throws CredencialesInvalidasException Si el usuario o la clave no coinciden en la BD.
	 */
	public Usuario autenticar(String email, String password) {
		
		Usuario u = usuarioDao.login(email, password);
		if (u == null) {
			throw new CredencialesInvalidasException("Credenciales incorrectas.");
		}
		
		u.setAutenticado(true);
		this.usuarioAutenticado = u;
		return u;
	}
	
	/**
	 * Busca el saldo disponible actual que tiene el usuario conectado en su cuenta.
	 * @return Los euros totales que tiene la cuenta en formato double.
	 * @throws DatoInvalidoException Si se intenta consultar sin haber iniciado sesión antes.
	 * @throws CuentaNoEncontradaException Si el usuario conectado no tiene ninguna cuenta a su nombre.
	 */
	public double obtenerSaldoActual() {
		if (usuarioAutenticado == null) {
			throw new DatoInvalidoException("No hay ningún usuario identificado en la sesión.");
		}
		
		String numCuenta = cuentaDao.buscarNumeroCuentaPorDni(usuarioAutenticado.getDni());
		
		if (numCuenta == null || numCuenta.trim().isEmpty()) {
			throw new CuentaNoEncontradaException("El usuario no tiene ninguna cuenta vinculada en el sistema.");
		}
		
		return cuentaDao.consultarSaldo(numCuenta);
	}
	
	/**
	 * Devuelve el objeto del usuario que tiene la sesión iniciada en este momento.
	 * @return El objeto Usuario autenticado actualmente en memoria.
	 */
	public Usuario getUsuarioAutenticado() {
		return this.usuarioAutenticado;
	}
	
	/**
	 * Procesa un ingreso de dinero en efectivo en el cajero bancario.
	 * Valida la sesión, localiza la cuenta del cliente por su DNI, comprueba que el importe 
	 * sea positivo, actualiza el saldo en la BD y registra la operación en el historial.
	 * @param importe La cantidad de dinero en euros que se va a ingresar.
	 * @throws DatoInvalidoException Si no hay sesión iniciada o el importe es menor o igual a 0 €.
	 * @throws CuentaNoEncontradaException Si no se localiza la cuenta bancaria del cliente.
	 */
	public void ingresarDinero(double importe) {
		
		if (usuarioAutenticado == null) {
			throw new DatoInvalidoException("No hay ningún usuario identificado.");
		}
		
		String numCuenta = cuentaDao.buscarNumeroCuentaPorDni(usuarioAutenticado.getDni());
		
		if (numCuenta == null) {
			throw new CuentaNoEncontradaException("No se encontró ninguna cuenta vinculada al usuario.");
		}
		
		if (importe <= 0) {
			throw new DatoInvalidoException("El importe a ingresar debe ser mayor que 0 €.");
		}
		
		double saldoActual = cuentaDao.consultarSaldo(numCuenta);
		
		double nuevoSaldo = saldoActual + importe;
		
		cuentaDao.actualizarSaldo(numCuenta, nuevoSaldo);
		
		Operacion ingreso = new Efectivo(importe, "Cliente", "Cajero", "COMPLETADA", null, numCuenta);
		operacionDao.registrarOperacion(ingreso, "EFECTIVO");
	}

	/**
	 * Procesa una retirada de dinero en efectivo desde el cajero.
	 * Verifica la sesión, comprueba que el importe sea correcto, valida que haya saldo 
	 * disponible suficiente y actualiza los datos además de añadir el movimiento al historial.
	 * @param importe La cantidad de euros en efectivo que se van a sacar.
	 * @throws DatoInvalidoException Si el usuario no está logueado o el dinero pedido es menor o igual a 0 €.
	 * @throws CuentaNoEncontradaException Si el cliente no tiene cuentas vinculadas.
	 * @throws SaldoInsuficienteException Si el dinero que pide el cliente supera el saldo actual de la cuenta.
	 */
	public void retirarDinero(double importe) {
		if (usuarioAutenticado == null) {
			throw new DatoInvalidoException("No hay ningún usuario identificado.");
		}
		
		String numCuenta = cuentaDao.buscarNumeroCuentaPorDni(usuarioAutenticado.getDni());
		
		if (numCuenta == null) {
			throw new CuentaNoEncontradaException("No se encontró ninguna cuenta vinculada al usuario.");
		}
		
		if (importe <= 0) {
			throw new DatoInvalidoException("El importe a retirar debe ser mayor que 0 €.");
		}
		
		double saldoActual = cuentaDao.consultarSaldo(numCuenta);
		
		if (saldoActual < importe) {
			throw new SaldoInsuficienteException("Saldo insuficiente. Saldo disponible: " + saldoActual + " €");
		}
		
		double nuevoSaldo = saldoActual - importe;
		cuentaDao.actualizarSaldo(numCuenta, nuevoSaldo);
		
		Operacion retirada = new Efectivo(importe, "Cliente", "Cajero", "COMPLETADA", numCuenta, null);
		operacionDao.registrarOperacion(retirada, "EFECTIVO");
	}
	
	/**
	 * Recupera la lista de todas las operaciones realizadas por el usuario actual.
	 * Localiza primero el IBAN del usuario y le pide al DAO el listado de movimientos de esa cuenta.
	 * @return Una lista de cadenas de texto (List de String) con el historial listo para imprimir.
	 * @throws DatoInvalidoException Si no hay ningún usuario logueado en el sistema.
	 * @throws CuentaNoEncontradaException Si no se encuentra la cuenta bancaria del usuario.
	 */
	public List<String> obtenerMovimientosUsuario() {
		
		if (usuarioAutenticado == null) {
			throw new DatoInvalidoException("No hay ningún usuario identificado en el sistema.");
		}
		
		String numCuenta = cuentaDao.buscarNumeroCuentaPorDni(usuarioAutenticado.getDni());
		
		if (numCuenta == null) {
			throw new CuentaNoEncontradaException("No se encontró ninguna cuenta bancaria vinculada a este usuario.");
		}
		
		return operacionDao.obtenerMovimientosPorCuenta(numCuenta);
	}
}