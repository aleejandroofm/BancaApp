package Vista;

import java.util.Scanner;
import Controlador.Controlador;
import Util.ErrorHandler;

/**
 * Clase que gestiona la vista de inicio de sesión de la aplicación (capa Vista del MVC).
 * Representa la pantalla de acceso principal, encargada de pedir el correo y la 
 * contraseña al usuario por consola y enviárselos al controlador.
 */
public class LoginView implements ErrorHandler.ErrorDisplay {
	
	private Scanner sc;
	private Controlador ct;
	
	/**
	 * Constructor por defecto. Inicializa el Scanner para leer por consola
	 * y crea la instancia del Controlador principal.
	 */
	public LoginView() {
		this.sc = new Scanner(System.in);
		this.ct = new Controlador();
	}
	
	/**
	 * Muestra la interfaz de inicio de sesión por consola.
	 * Pide al usuario que introduzca su correo electrónico y su contraseña,
	 * y le pasa estos datos directamente al controlador para iniciar el proceso de autenticación.
	 */
	public void mostrarLogin() {
		System.out.println("--- ACCESO AL SISTEMA BANCARIO ---");
		
		System.out.print("Introduzca su correo: ");
		String email = sc.nextLine();
		
		System.out.print("Introduzca su contraseña: ");
		String password = sc.nextLine();
		
		ct.iniciarSesion(email, password, this);
	}
		
	/**
	 * Método sobrescrito de la interfaz ErrorDisplay.
	 * Se encarga de detallar el código y el motivo del error
	 * cuando el inicio de sesión falla (credenciales incorrectas, etc.).
	 * @param codigo El código identificador del error que ha ocurrido.
	 * @param mensaje El texto explicativo con los detalles del fallo.
	 */
	@Override
	public void mostrarError(String codigo, String mensaje) {
		System.out.println("==================================================");
		System.out.println(" ERROR EN EL SISTEMA [" + codigo + "]");
		System.out.println(" -> " + mensaje);
		System.out.println("==================================================");
	}
	
	/**
	 * Muestra un mensaje de éxito por consola una vez que el usuario se ha logueado correctamente.
	 * @param nombre El nombre del usuario o cliente que acaba de entrar al sistema.
	 */
	public void mostrarMensajeBienvenida(String nombre) {
		System.out.println("--- Acceso concedido ---");
		System.out.println("--- Bienvenido/a, " + nombre + " ---");
	}
}