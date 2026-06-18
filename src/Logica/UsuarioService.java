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
import Modelo.TipoOperacion;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los usuarios.
 * Controla la sesión del usuario autenticado y coordina operaciones bancarias
 * como ingresos, retiradas y consulta de movimientos.
 */
public class UsuarioService {
	
	private UsuarioDAO usuarioDao = new UsuarioDAO();
	private CuentaDAO cuentaDao = new CuentaDAO();
	private Usuario usuarioAutenticado;
	private OperacionDAO operacionDao = new OperacionDAO();
	
	/**
	 * Autentica un usuario en el sistema mediante email y contraseña.
	 * Si las credenciales son correctas, se almacena el usuario como sesión activa.
	 *
	 * @param email correo electrónico del usuario.
	 * @param password contraseña del usuario.
	 * @return objeto Usuario autenticado.
	 * @throws CredencialesInvalidasException si las credenciales no son válidas.
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
	 * Obtiene el saldo actual de la cuenta del usuario autenticado.
	 *
	 * @return saldo disponible en la cuenta.
	 * @throws DatoInvalidoException si no hay sesión iniciada.
	 * @throws CuentaNoEncontradaException si el usuario no tiene cuenta asociada.
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
	 * Devuelve el usuario actualmente autenticado en el sistema.
	 *
	 * @return usuario en sesión o null si no hay sesión activa.
	 */
	public Usuario getUsuarioAutenticado() {
		return this.usuarioAutenticado;
	}
	
	/**
	 * Realiza un ingreso de dinero en la cuenta del usuario autenticado.
	 *
	 * @param importe cantidad a ingresar.
	 * @throws DatoInvalidoException si no hay sesión o el importe es inválido.
	 * @throws CuentaNoEncontradaException si no existe cuenta asociada.
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
		
		Operacion ingreso = new Efectivo(
			importe,
			"Cliente",
			"Cajero",
			"COMPLETADA",
			null,
			numCuenta
		);
		
		operacionDao.registrarOperacion(ingreso, TipoOperacion.EFECTIVO);
	}
	
	/**
	 * Realiza una retirada de dinero de la cuenta del usuario autenticado.
	 *
	 * @param importe cantidad a retirar.
	 * @throws DatoInvalidoException si no hay sesión o el importe es inválido.
	 * @throws CuentaNoEncontradaException si no existe cuenta asociada.
	 * @throws SaldoInsuficienteException si el saldo es insuficiente.
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
			throw new SaldoInsuficienteException(
				"Saldo insuficiente. Saldo disponible: " + saldoActual + " €"
			);
		}
		
		double nuevoSaldo = saldoActual - importe;
		cuentaDao.actualizarSaldo(numCuenta, nuevoSaldo);
		
		Operacion retirada = new Efectivo(
			importe,
			"Cliente",
			"Cajero",
			"COMPLETADA",
			numCuenta,
			null
		);
		
		operacionDao.registrarOperacion(retirada, TipoOperacion.EFECTIVO);
	}
	
	/**
	 * Obtiene el historial de movimientos del usuario autenticado.
	 * Versión segura que recibe el DNI para evitar dependencias de estado inconsistente.
	 *
	 * @param dni DNI del usuario autenticado.
	 * @return lista de movimientos formateados.
	 * @throws DatoInvalidoException si el DNI es nulo o vacío.
	 * @throws CuentaNoEncontradaException si no existe cuenta asociada al usuario.
	 */
	public List<String> obtenerMovimientosUsuario(String dni) {
		
		if (dni == null || dni.isEmpty()) {
			throw new DatoInvalidoException("No hay ningún usuario identificado en el sistema.");
		}
		
		String numCuenta = cuentaDao.buscarNumeroCuentaPorDni(dni);
		
		if (numCuenta == null) {
			throw new CuentaNoEncontradaException(
				"No se encontró ninguna cuenta bancaria vinculada a este usuario."
			);
		}
		
		return operacionDao.obtenerMovimientosPorCuenta(numCuenta);
	}
	
	/**
	 * Método de compatibilidad con versiones anteriores.
	 * Usa el usuario en sesión para obtener su historial de movimientos.
	 *
	 * @return lista de movimientos del usuario autenticado.
	 * @throws DatoInvalidoException si no hay sesión iniciada.
	 */
	public List<String> obtenerMovimientosUsuario() {
		
		if (usuarioAutenticado == null) {
			throw new DatoInvalidoException("No hay ningún usuario identificado en el sistema.");
		}
		
		return obtenerMovimientosUsuario(usuarioAutenticado.getDni());
	}
}