package Vista;

import java.util.List;
import java.util.Scanner;
import Controlador.Controlador;
import Util.ErrorHandler;

/**
 * Clase que gestiona el menú interactivo para los clientes (capa Vista del MVC).
 * Ofrece opciones para consultar saldo, ingresar, retirar, transferir dinero 
 * y revisar el historial de movimientos directamente por la consola.
 */
public class ClienteView implements ErrorHandler.ErrorDisplay {
    
    private Scanner sc;
    
    /**
     * Constructor por defecto. Inicializa el lector de teclado (Scanner)
     * para capturar las opciones y los importes del cliente.
     */
    public ClienteView() {
        this.sc = new Scanner(System.in);
    }
    
    /**
     * Despliega el bucle principal con el menú de opciones del cliente.
     * Captura la opción elegida, procesa los datos necesarios para cada operación
     * (como importes o IBANs) y se los manda al controlador para que ejecute la lógica.
     * @param ct Instancia del Controlador principal para comunicar la vista con el servicio.
     * @param nombreCliente El nombre del cliente logueado para personalizar los mensajes.
     */
    public void mostrarMenuCliente(Controlador ct, String nombreCliente) {
        
        int opcion = 0;
        do {
            System.out.println("\n===== MENÚ PRINCIPAL - CLIENTE =====");
            System.out.println("1. Consultar Saldo.");
            System.out.println("2. Ingresar Dinero.");
            System.out.println("3. Retirar Dinero.");
            System.out.println("4. Realizar Transferencia.");
            System.out.println("5. Ver Movimientos.");
            System.out.println("6. Cerrar Sesión.");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(sc.nextLine());
                
                switch(opcion) {
                    case 1 -> {
                        try {
                        	
                            System.out.println("\n===== CONSULTAR SALDO =====");
                            double saldo = ct.consultarSaldo();
                            mostrarSaldo(saldo);
                        } catch (Exception e) {
                            ErrorHandler.gestionar(e, this);
                        }
                    }
                    
                    case 2 -> {
                        System.out.println("\n===== INGRESAR DINERO =====");
                        try {
                        	
                            System.out.print("Introduce el importe a ingresar (€): ");
                            double importe = Double.parseDouble(sc.nextLine());
                            
                            ct.ingresar(importe);
                            System.out.println("¡Ingreso realizado con éxito!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe introducido no tiene un formato válido.");
                        } catch (Exception e) {
                            ErrorHandler.gestionar(e, this);
                        }
                    }
                    
                    case 3 -> {
                        System.out.println("\n===== RETIRAR DINERO =====");
                        try {
                        	
                            System.out.print("Introduce el importe a retirar (€): ");
                            double importe = Double.parseDouble(sc.nextLine());
                            
                            ct.retirar(importe);
                            System.out.println("¡Se ha retirado el importe con éxito!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe introducido no tiene un formato válido.");
                        } catch (Exception e) {
                            ErrorHandler.gestionar(e, this);
                        }
                    }
                    
                    case 4 -> {
                        System.out.println("\n===== REALIZAR TRANSFERENCIA =====");
                        System.out.print("Introduce el numero de cuenta destino (IBAN): ");
                        String cuentaDestino = sc.nextLine();
                        
                        System.out.print("Introduce el importe a transferir (€): ");
                        try {
                        	
                            double importe = Double.parseDouble(sc.nextLine());
   
                            ct.realizarTransferencia(cuentaDestino, importe);
                            System.out.println("¡Transferencia realizada con exito!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe introducido no tiene un formato valido.");
                        } catch (Exception e) {
                            ErrorHandler.gestionar(e, this);
                        }
                    }
                    
                    case 5 -> {
                        System.out.println("\n========================================");
                        System.out.println("       HISTORIAL DE MOVIMIENTOS         ");
                        System.out.println("========================================");
                        try {
                        	
                            List<String> historial = ct.verMovimientos();
                            
                            if (historial.isEmpty()) {
                                System.out.println(" No se han registrado movimientos en esta cuenta.");
                            } else {
                                for (String movimiento : historial) {
                                    System.out.println(movimiento);
                                }
                            }
                        } catch (Exception e) {
                            ErrorHandler.gestionar(e, this); 
                        }
                    }
                    
                    case 6 -> {
                        System.out.println("Cerrando sesion...");
                    }
                    
                    default -> System.out.println("Opcion no valida. Intentelo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca un numero entero valido.");
            }
        } while (opcion != 6);
    }
    
    /**
     * Pinta en consola el saldo actual formateado de forma limpia y clara para el usuario.
     * @param saldo El saldo total disponible en la cuenta del cliente.
     */
    public void mostrarSaldo(double saldo) {
        System.out.println("------------------------------------");
        System.out.printf("   SALDO ACTUAL: %.2f €\n", saldo);
        System.out.println("------------------------------------");
    }
    
    /**
     * Método heredado de la interfaz ErrorDisplay.
     * Captura las excepciones de la aplicación y muestra un bloque visual de error
     * para avisar al cliente de fallos como saldos insuficientes o cuentas inexistentes.
     * @param codigo Código que identifica el tipo de excepción lanzada.
     * @param mensaje Texto explicativo detallando qué ha fallado.
     */
    @Override
    public void mostrarError(String codigo, String mensaje) {
        System.out.println("==================================================");
        System.out.println(" ERROR EN OPERACIÓN CLIENTE [" + codigo + "]");
        System.out.println(" -> " + mensaje);
        System.out.println("==================================================\n");
    }
}