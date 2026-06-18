package Vista;

import java.util.List;
import java.util.Scanner;
import Controlador.Controlador;
import Util.ErrorHandler;
import Excepciones.Cuenta.CuentaInactivaException;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.LimiteExcedidoException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Excepciones.Operacion.DestinatarioInvalidoException;
import Excepciones.Sistema.PersistenciaException;

/**
 * Clase que gestiona el menú interactivo para los clientes (capa Vista del MVC).
 * Ofrece opciones para operar con cuentas e historial directamente por consola.
 * @author Alejandro Ferrándiz Martínez
 */
public class ClienteView implements ErrorHandler.ErrorDisplay {
    
    private Scanner sc;
   
    public ClienteView() {
        this.sc = new Scanner(System.in);
    }
    
    /**
     * Despliega el bucle principal con el menú de opciones del cliente.
     * @param ct Instancia del Controlador principal.
     * @param nombreCliente El nombre del cliente logueado.
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
            System.out.println("6. Realizar Bizum.");
            System.out.println("7. Cerrar Sesión.");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(sc.nextLine());
                
                switch(opcion) {
                    case 1 -> {
                        System.out.println("\n===== CONSULTAR SALDO =====");
                        try {
                            double saldo = ct.consultarSaldo();
                            mostrarSaldo(saldo);
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-CUENTA-404", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-DB-500", "Error de comunicación con el banco.");
                        }
                    }
                    
                    case 2 -> {
                        System.out.println("\n===== INGRESAR DINERO =====");
                        System.out.print("Introduce el importe a ingresar (€): ");    
                        try {
                            
                            double importe = Double.parseDouble(sc.nextLine());
                            
                            ct.ingresar(importe);
                            System.out.println("¡Ingreso realizado con éxito!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe introducido no tiene un formato válido.");
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-CUENTA-404", e.getMessage());
                            
                        } catch (DatoInvalidoException e) {
                            mostrarError("ERR-VAL-400", e.getMessage());
                            
                        } catch (CuentaInactivaException e) {
                            mostrarError("ERR-BLOQUEO-403", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-DB-500", "No se pudo consolidar el ingreso.");
                        }
                    }
                    
                    case 3 -> {
                        System.out.println("\n===== RETIRAR DINERO =====");
                        System.out.print("Introduce el importe a retirar (€): ");
                        try {
                            
                            double importe = Double.parseDouble(sc.nextLine());
                            
                            ct.retirar(importe); 
                            System.out.println("¡Se ha retirado el importe con éxito!");
                            
                        } catch (NumberFormatException e) {
                            mostrarError("ERR-FORMATO-400", "El importe debe ser un número decimal válido.");
                            
                        } catch (SaldoInsuficienteException e) {
                            mostrarError("ERR-SALDO-400", e.getMessage());
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-CUENTA-404", e.getMessage());
                            
                        } catch (CuentaInactivaException e) {
                            mostrarError("ERR-ESTADO-403", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-DB-500", e.getMessage());
                        }
                    }
                    
                    case 4 -> {
                        System.out.println("\n===== REALIZAR TRANSFERENCIA =====");
                        System.out.print("Introduce el número de cuenta destino (IBAN): ");
                        String cuentaDestino = sc.nextLine();
                        System.out.print("Introduce el importe a transferir (€): ");
                        
                        try {
                            double importe = Double.parseDouble(sc.nextLine());
                            ct.realizarTransferencia(cuentaDestino, importe);
                            System.out.println("¡Transferencia realizada con éxito!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe debe ser un número decimal.");
                            
                        } catch (DestinatarioInvalidoException e) {
                            mostrarError(e.getCodigoError(), e.getMessage());
                            
                        } catch (IllegalArgumentException e) {
                            mostrarError("ERR-TR-MISMA-CUENTA", e.getMessage());
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-TR-404", e.getMessage());
                            
                        } catch (CuentaInactivaException e) {
                            mostrarError("ERR-BLOQUEO-403", e.getMessage());
                            
                        } catch (DatoInvalidoException e) {
                            mostrarError("ERR-TR-400", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-TR-500", e.getMessage());
                        }
                    }
                    
                    case 5 -> {
                        System.out.println("\n========================================");
                        System.out.println("        HISTORIAL DE MOVIMIENTOS        ");
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
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-DB-500", "No se pudo recuperar el historial de movimientos.");
                        }
                    }
                    

                    case 6 -> {
                        System.out.println("\n===== ENVIAR BIZUM =====");
                        System.out.print("Introduce el número de teléfono destino: ");
                        String telefonoDestino = sc.nextLine();
                        System.out.print("Introduce el importe a enviar (€): ");
                        
                        try {
                            double importe = Double.parseDouble(sc.nextLine());
                            
                            ct.enviarBizum(telefonoDestino, importe);
                            System.out.println("¡Bizum enviado con éxito!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe introducido debe ser un número válido.");
                            
                        } catch (DestinatarioInvalidoException e) {
                            mostrarError(e.getCodigoError(), e.getMessage());
                            
                        } catch (LimiteExcedidoException e) {
                            mostrarError("ERR-BIZUM-LIMIT", e.getMessage());
                            
                        } catch (SaldoInsuficienteException e) {
                            mostrarError("ERR-SALDO-400", e.getMessage());
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-BIZUM-404", e.getMessage());
                            
                        } catch (CuentaInactivaException e) {
                            mostrarError("ERR-BLOQUEO-403", e.getMessage());
                            
                        } catch (DatoInvalidoException e) {
                            mostrarError("ERR-DATOS-400", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-DB-500", e.getMessage());
                        }
                    }
                    
                    case 7 -> System.out.println("Cerrando sesión de cliente...");
                    
                    default -> System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca un número entero válido.");
            }
        } while (opcion != 7);
    }
    
    /**
     * Muestra el saldo en consola
     * @param saldo Saldo disponible.
     */
    public void mostrarSaldo(double saldo) {
        System.out.println("------------------------------------");
        System.out.printf("   SALDO ACTUAL: %.2f €\n", saldo);
        System.out.println("------------------------------------");
    }
    
    @Override
    public void mostrarError(String codigo, String mensaje) {
        System.out.println("\n==================================================");
        System.out.println(" ERROR EN OPERACIÓN CLIENTE [" + codigo + "]");
        System.out.println(" -> " + mensaje);
        System.out.println("==================================================\n");
    }
}