package Controlador;

import java.util.List;
import Logica.TransferenciaService;
import Logica.UsuarioService;
import Modelo.Usuario;
import Vista.LoginView;
import Vista.ClienteView;
import Util.ErrorHandler;

/**
 * Clase controlador principal de la aplicación (capa Controlador del MVC).
 * Centraliza las peticiones de las vistas de la aplicación y redirige el flujo
 * hacia los servicios de lógica correspondientes (usuarios o transferencias).
 */
public class Controlador {
	
	private UsuarioService userService = new UsuarioService();
	private TransferenciaService transferService = new TransferenciaService();
	
	/**
	 * Gestiona el proceso de inicio de sesión de un usuario.
	 * Llama al servicio para autenticar las credenciales y, si son correctas,
	 * da la bienvenida al usuario y le abre de golpe el menú principal del cliente.
	 * @param email Correo electrónico introducido en el login.
	 * @param password Contraseña introducida en el login.
	 * @param lg La vista de login activa para poder pintar los mensajes de error si falla.
	 */
	public void iniciarSesion(String email, String password, LoginView lg) {
		
		try {
			Usuario user = userService.autenticar(email, password);
			lg.mostrarMensajeBienvenida(user.getNombre());
			
			ClienteView menuCliente = new ClienteView();
			menuCliente.mostrarMenuCliente(this, user.getNombre());
			
		} catch (Exception e) {
			ErrorHandler.gestionar(e, lg);
		}
	}
	
	/**
	 * Pide al servicio de usuarios el saldo disponible de la cuenta conectada.
	 * @return El saldo actual de la cuenta en formato double.
	 */
	public double consultarSaldo() {
		return userService.obtenerSaldoActual();
	}
	
	/**
	 * Sirve de puente para mandar una orden de transferencia entre cuentas.
	 * Extrae automáticamente el DNI del usuario logueado en la sesión
	 * y se lo pasa junto al destino y el dinero al servicio de transferencias.
	 * @param cuentaDestino El IBAN de la cuenta que va a recibir el dinero.
	 * @param importe La cantidad de euros a transferir.
	 */
	public void realizarTransferencia(String cuentaDestino, double importe) {
		String dniOrigen = userService.getUsuarioAutenticado().getDni();
		transferService.realizarTransferencia(dniOrigen, cuentaDestino, importe);
	}
	
	/**
	 * Comunica la petición de un ingreso de dinero en efectivo con el servicio.
	 * @param importe Los euros en físico que se van a depositar.
	 */
	public void ingresar(double importe) {
		userService.ingresarDinero(importe);
	}
	
	/**
	 * Comunica la petición de una retirada de dinero en efectivo con el servicio.
	 * @param importe Los euros que el cliente desea sacar del cajero.
	 */
	public void retirar(double importe) {
		userService.retirarDinero(importe);
	}
	
	/**
	 * Solicita la lista completa con el historial de movimientos de la cuenta actual.
	 * @return Una lista de cadenas de texto (List de String) con los movimientos formateados.
	 */
	public List<String> verMovimientos() {
		return userService.obtenerMovimientosUsuario();
	}
}