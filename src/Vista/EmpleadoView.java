package Vista;

import java.util.List;
import java.util.Scanner;
import Controlador.Controlador;
import Util.ErrorHandler;
import Validaciones.ValidadorIBAN;
import Excepciones.Autenticacion.UsuarioYaExisteException;
import Excepciones.Cuenta.CuentaInactivaException;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Excepciones.Sistema.PersistenciaException;

/**
 * Vista de consola para el perfil de Empleado del sistema bancario.
 * Gestiona trámites administrativos y operativas de ventanilla.
 * @author Alejandro Ferrándiz Martínez
 */
public class EmpleadoView implements ErrorHandler.ErrorDisplay {
    
    private Scanner sc;
   
    public EmpleadoView() {
        this.sc = new Scanner(System.in);
    }
    
    public void mostrarMenuEmpleado(Controlador ct, String nombreEmpleado) {
        
        int opcion = 0;
        do {
            System.out.println("\n===== PANEL DE CONTROL BANCARIO - EMPLEADO =====");
            System.out.println("1. Registrar Nuevo Cliente.");
            System.out.println("2. Bloquear / Activar Cuenta.");
            System.out.println("3. Ingreso de Efectivo en Ventanilla.");
            System.out.println("4. Retirada de Efectivo en Ventanilla.");
            System.out.println("5. Dar de Baja a Un Usuario.");
            System.out.println("6. Ver el Historial Global de Operaciones.");
            System.out.println("7. Cerrar Sesión.");
            System.out.print("Seleccione una opción de gestión: ");
            
            try {
                opcion = Integer.parseInt(sc.nextLine());
                
                switch(opcion) {
                    case 1 -> {
                        System.out.println("\n===== ALTA DE CLIENTE =====");
                        System.out.print("DNI del nuevo cliente: ");
                        String dni = sc.nextLine();
                        System.out.print("Nombre completo: ");
                        String nombre = sc.nextLine();
                        System.out.print("Email de acceso: ");
                        String email = sc.nextLine();
                        System.out.print("Contraseña provisional: ");
                        String password = sc.nextLine();
                        System.out.print("Teléfono móvil: ");
                        String telefono = sc.nextLine();
                        System.out.print("Asignar número de cuenta: ");
                        String iban = sc.nextLine();
                        System.out.print("Depósito de apertura obligatorio (€): ");
                        
                        try {
                            double saldoInicial = Double.parseDouble(sc.nextLine());
                            ct.registrarClientePorEmpleado(dni, nombre, email, password, telefono, iban, saldoInicial);
                            System.out.println("¡Éxito! Cliente guardado en el sistema y cuenta [" + iban + "] activada.");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El depósito inicial debe ser un número válido.");
                            
                        } catch (UsuarioYaExisteException e) {
                            mostrarError("ERR_USUARIO_DUPLICADO", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-BD-500", "Fallo al registrar los datos en MySQL.");
                        }
                    }
                    
                    case 2 -> {
                        System.out.println("\n===== BLOQUEAR / ACTIVAR CUENTA =====");
                        System.out.print("Introduce el IBAN del cliente a modificar: ");
                        String iban = sc.nextLine();
                        System.out.println("Selecciona el estado operativo:");
                        System.out.println("1. Habilitar / Activar cuenta (Permitir operativas).");
                        System.out.println("2. Bloquear cuenta (Inmovilizar fondos).");
                        System.out.print("Opción: ");
                        
                        try {
                            int seleccion = Integer.parseInt(sc.nextLine());
                            if (seleccion != 1 && seleccion != 2) {
                                System.out.println("Opción inválida. Trámite cancelado.");
                            } else {
                                boolean nuevoEstado = (seleccion == 1);
                                ct.cambiarEstadoCuenta(iban, nuevoEstado);
                                String descripcionEstado = nuevoEstado ? "ACTIVA" : "BLOQUEADA";
                                System.out.println("¡Éxito! La cuenta [" + iban + "] pasa a estar " + descripcionEstado + ".");
                            }
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Selecciona introduciendo 1 o 2.");
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-CUENTA-404", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-BD-500", "Error al guardar el log de bloqueo.");
                        }
                    }
                    
                    case 3 -> {
                        System.out.println("\n===== INGRESO EN EFECTIVO DESDE VENTANILLA =====");
                        System.out.print("Introduce el IBAN del cliente beneficiario: ");
                        String iban = sc.nextLine();
                        System.out.print("Importe a ingresar (€): ");
                        
                        try {
                            double importe = Double.parseDouble(sc.nextLine());
                            ct.ingresarEnVentanilla(iban, importe);
                            System.out.println("¡Ingreso realizado con éxito! Imprima el recibo para el cliente.");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe debe ser un número válido.");
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-ING-404", e.getMessage());
                            
                        } catch (CuentaInactivaException e) {
                            mostrarError("ERR-BLOQUEO-403", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-BD-500", "No se pudo actualizar el saldo en la base de datos.");
                        }
                    }
                    
                    case 4 -> {
                        System.out.println("\n===== RETIRADA EN EFECTIVO DESDE VENTANILLA =====");
                        System.out.print("Introduce el IBAN del cliente solicitante: ");
                        String iban = sc.nextLine();
                        System.out.print("Importe a retirar (€): ");
                        
                        try {
                            double importe = Double.parseDouble(sc.nextLine());
                            ct.retirarEnVentanilla(iban, importe); 
                            System.out.println("¡Retirada autorizada!");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El importe debe ser un número válido.");
                            
                        } catch (SaldoInsuficienteException e) {
                            mostrarError("ERR-SALDO-400", e.getMessage());
                            
                        } catch (CuentaNoEncontradaException e) {
                            mostrarError("ERR-CUENTA-404", e.getMessage());
                            
                        } catch (CuentaInactivaException e) {
                            mostrarError("ERR-BLOQUEO-403", e.getMessage());
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-BD-500", e.getMessage());
                        }
                    }
                    
                    case 5 -> {
                        System.out.println("\n===== ELIMINAR / DAR DE BAJA CUENTA BANCARIA =====");
                        System.out.print("Introduce el IBAN de la cuenta que deseas ELIMINAR: ");
                        String iban = sc.nextLine();
                        System.out.print("Introduce el DNI del titular de la cuenta: ");
                        String dni = sc.nextLine();
                        
                        ValidadorIBAN.validarFormato(iban);
                        
                        System.out.println("¡ADVERTENCIA! Esta acción borrará la cuenta del sistema de forma irreversible.");
                        System.out.print("¿Está seguro de que desea continuar?: ");
                        String confirmacion = sc.nextLine();
                        
                        if (confirmacion.equalsIgnoreCase("SI")) {
                            try {
                                ct.eliminarUsuarioPorEmpleado(iban, dni);
                                System.out.println("¡La cuenta [" + iban + "] ha sido eliminada del sistema correctamente!");
                                
                            } catch (CuentaNoEncontradaException e) {
                                mostrarError("ERR-CUENTA-404", e.getMessage());
                                
                            } catch (PersistenciaException e) {
                                mostrarError("ERR-BD-500", "Fallo de integridad o restricción de clave foránea en MySQL: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Operación cancelada de forma segura. La cuenta no ha sufrido modificaciones.");
                        }
                    }
                    
                    case 6 -> {
                        System.out.println("\n===== HISTORIAL GLOBAL DE OPERACIONES FINANCIERAS =====");
                        try {
                            List<String[]> historial = ct.obtenerAuditoriaBanco();
                            
                            if (historial.isEmpty()) {
                                System.out.println("No hay registros de auditoría en la base de datos.");
                            } else {
                                System.out.println("------------------------------------------------------------------------------------------------------------------");
                                System.out.printf("%-10s | %-15s | %-12s | %-22s | %-22s | %-20s\n", "ID OP", "TIPO", "IMPORTE", "CUENTA ORIGEN", "CUENTA DESTINO", "FECHA");
                                System.out.println("------------------------------------------------------------------------------------------------------------------");
                                
                                for (String[] fila : historial) {
                                    System.out.printf("%-10s | %-15s | %-12s | %-22s | %-22s | %-20s\n",
                                        fila[0], // idOperacion
                                        fila[1], // tipoOperacion
                                        fila[2] + "€", // importe
                                        fila[3], // idCuentaOrigen
                                        fila[4], // idCuentaDestino
                                        fila[5]  // fechaOperacion
                                    );
                                }
                                System.out.println("------------------------------------------------------------------------------------------------------------------");
                            }
                            
                        } catch (PersistenciaException e) {
                            mostrarError("ERR-AUDIT-500", e.getMessage());
                        }
                    }
                    
                    case 7 -> System.out.println("Cerrando sesión...");
                    
                    default -> System.out.println("Opción no disponible en el panel de empleados.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca un número entero válido.");
            }
        } while (opcion != 7); // 🟢 CORRECCIÓN: Cambiado de 6 a 7 para que no se salga al auditar
    }
    
    @Override
    public void mostrarError(String codigo, String mensaje) {
        System.out.println("\n==================================================");
        System.out.println(" ERROR DE SUCURSAL BANCARIA [" + codigo + "]");
        System.out.println(" -> " + mensaje);
        System.out.println("==================================================\n");
    }
}