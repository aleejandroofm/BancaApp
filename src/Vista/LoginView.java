package Vista;

import java.util.Scanner;
import Controlador.Controlador;
import Modelo.Rol;
import Modelo.Usuario;
import Util.ErrorHandler;
import Excepciones.Autenticacion.CredencialesInvalidasException;
import Excepciones.Autenticacion.UsuarioBloqueadoException;
import Excepciones.Sistema.PersistenciaException;

/**
 * Vista de inicio de sesión de la aplicación bancaria.
 * Encargada de capturar credenciales y redirigir al menú correspondiente.
 * @author Alejandro Ferrándiz Martínez
 */
public class LoginView implements ErrorHandler.ErrorDisplay {

    private Scanner sc;
    private Controlador ct;

    public LoginView(Controlador ct) {
        this.sc = new Scanner(System.in);
        this.ct = ct;
    }

    /**
     * Muestra el formulario de inicio de sesión en un bucle hasta que el acceso es correcto o se fuerza la salida.
     */
    public void mostrarLogin() {
        boolean accesoCorrecto = false;

        do {
            System.out.println("\n--- ACCESO AL SISTEMA BANCARIO ---");
            System.out.print("Introduzca su correo: ");
            String email = sc.nextLine();


            System.out.print("Introduzca su contraseña: ");
            String password = sc.nextLine();

            try {
                
                Usuario u = ct.iniciarSesion(email, password);
                accesoCorrecto = true; 
                
                mostrarMensajeBienvenida(u.getNombre());

                // Enrutamiento según el Rol del usuario
                if (u.getRol() == Rol.CLIENTE) {
                    
                    ClienteView cv = new ClienteView();
                    cv.mostrarMenuCliente(ct, u.getNombre());
                    
                } else if (u.getRol() == Rol.EMPLEADO) {
                    EmpleadoView ev = new EmpleadoView();
                    ev.mostrarMenuEmpleado(ct, u.getNombre());
                }

            } catch (CredencialesInvalidasException e) {
                
                mostrarError("ERR-AUTH-401", e.getMessage());
                
            } catch (UsuarioBloqueadoException e) {
                mostrarError(e.getCodigoError(), e.getMessage());
                
            } catch (PersistenciaException e) {
                mostrarError("ERR-DB-500", "No se ha podido conectar con la base de datos: " + e.getMessage());
            }

        } while (!accesoCorrecto); 
    }

    @Override
    public void mostrarError(String codigo, String mensaje) {
        System.out.println("\n==================================================");
        System.out.println(" ERROR EN EL SISTEMA [" + codigo + "]");
        System.out.println(" -> " + mensaje);
        System.out.println("==================================================");
    }

    /**
     * Muestra un mensaje de bienvenida personalizado en la consola.
     * @param nombre Nombre del usuario logueado.
     */
    public void mostrarMensajeBienvenida(String nombre) {
        System.out.println("\n--- Acceso concedido ---");

        Usuario usuarioActivo = ct.getUsuarioActivo();

        if (usuarioActivo != null) {
            System.out.println("--- Bienvenido/a, " + nombre + " [" + usuarioActivo.getRol() + "] ---");
        } else {
            System.out.println("--- Bienvenido/a, " + nombre + " ---");
        }
    }
}