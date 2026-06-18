package Controlador;

import java.util.List;
import Logica.TransferenciaService;
import Logica.UsuarioService;
import Logica.CuentaService;
import Logica.EmpleadoService;
import Modelo.Cliente;
import Modelo.Cuenta;
import Modelo.Rol;
import Modelo.Usuario;
import Dao.UsuarioDAO;
import Dao.CuentaDAO;
import Dao.OperacionDAO;
import Excepciones.Autenticacion.CredencialesInvalidasException;
import Excepciones.Autenticacion.UsuarioYaExisteException;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Operacion.DatoInvalidoException;
import Excepciones.Operacion.LimiteExcedidoException;
import Excepciones.Operacion.SaldoInsuficienteException;
import Excepciones.Sistema.PersistenciaException;

/**
 * Controlador principal que coordina el flujo entre las Vistas y la Lógica de negocio.
 * * @author Alejandro Ferrándiz Martínez
 */
public class Controlador {

    private UsuarioService userService = new UsuarioService();
    private TransferenciaService transferService = new TransferenciaService();
    private EmpleadoService empleadoService = new EmpleadoService();
    private CuentaService cuentaService = new CuentaService();
    private UsuarioDAO usuarioDao = new UsuarioDAO();
    private OperacionDAO operacionDao = new OperacionDAO();
    private CuentaDAO cuentaDao = new CuentaDAO();
    private Usuario usuarioActivo;

    /**
     * Valida las credenciales del usuario y guarda la sesión en memoria si es correcto.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return El usuario autenticado.
     * @throws CredencialesInvalidasException Si los datos de acceso no coinciden.
     * @throws PersistenciaException Si falla la conexión con la base de datos.
     */
    public Usuario iniciarSesion(String email, String password) throws CredencialesInvalidasException, PersistenciaException {
        this.usuarioActivo = userService.autenticar(email, password);

        if (this.usuarioActivo == null) {
            throw new CredencialesInvalidasException("Las credenciales introducidas no coinciden con ningún usuario.");
        }

        return this.usuarioActivo;
    }

    /**
     * Obtiene el saldo disponible de la cuenta asociada al usuario activo.
     * @return Saldo disponible.
     * @throws CuentaNoEncontradaException Si el usuario no tiene ninguna cuenta registrada.
     * @throws PersistenciaException Si ocurre un error en la consulta SQL.
     */
    public double consultarSaldo() throws CuentaNoEncontradaException, PersistenciaException {
        String numeroCuenta = usuarioDao.buscarNumeroCuentaPorDni(this.usuarioActivo.getDni());
        return cuentaDao.consultarSaldo(numeroCuenta);
    }

    /**
     * Tramita una transferencia bancaria entre la cuenta activa y una cuenta destino.
     * @param cuentaDestino IBAN de la cuenta receptora.
     * @param importe Cantidad de dinero a enviar.
     * @throws CuentaNoEncontradaException Si alguna de las cuentas no existe.
     * @throws DatoInvalidoException Si el importe es menor o igual a cero.
     * @throws PersistenciaException Si la transacción falla en la base de datos.
     */
    public void realizarTransferencia(String cuentaDestino, double importe) throws CuentaNoEncontradaException, DatoInvalidoException, PersistenciaException {
        
        String dniOrigen = this.usuarioActivo.getDni();
        transferService.realizarTransferencia(dniOrigen, cuentaDestino, importe);
    }

    /**
     * Realiza un ingreso de efectivo en la cuenta del usuario logueado.
     * @param importe Cantidad a ingresar.
     * @throws CuentaNoEncontradaException Si no se localiza la cuenta del usuario.
     * @throws PersistenciaException Si falla el comando UPDATE en la base de datos.
     */
    public void ingresar(double importe) throws CuentaNoEncontradaException, PersistenciaException {
        String numeroCuenta = usuarioDao.buscarNumeroCuentaPorDni(this.usuarioActivo.getDni());
        cuentaService.ingresarDinero(numeroCuenta, importe);
    }

    /**
     * Realiza una retirada de dinero en efectivo de la cuenta activa.
     * @param importe Cantidad a retirar.
     * @throws CuentaNoEncontradaException Si no se encuentra la cuenta asociada.
     * @throws DatoInvalidoException Si el importe supera el saldo disponible.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws SaldoInsuficienteException Si la cantidad a retirar es mayor que el saldo disponible.
     */
    public void retirar(double importe) throws CuentaNoEncontradaException, SaldoInsuficienteException, PersistenciaException {
        String numeroCuenta = usuarioDao.buscarNumeroCuentaPorDni(this.usuarioActivo.getDni());
        cuentaService.retirarDinero(numeroCuenta, importe); // Tu servicio ya lanza SaldoInsuficienteException
    }
    
    /**
     * Procesa el ingreso de efectivo en una cuenta corriente desde la ventanilla de empleados.
     * Este método actúa como intermediario en la capa de control, delegando la validación 
     * y la actualización del saldo en la capa de servicios financieros.
     * @param numeroCuenta Código IBAN de la cuenta corriente del cliente beneficiario.
     * @param importe Cantidad de dinero en metálico a depositar.
     * @throws CuentaNoEncontradaException Si el IBAN introducido no corresponde a ninguna cuenta registrada.
     * @throws CuentaInactivaException Si la cuenta destino está bloqueada o inactiva en el sistema.
     * @throws PersistenciaException Si ocurre un error relacional o de conectividad con la base de datos MySQL.
     */
    public void ingresarEnVentanilla(String numeroCuenta, double importe) throws CuentaNoEncontradaException, PersistenciaException {
        cuentaService.ingresarDinero(numeroCuenta, importe);
    }

    /**
     * Procesa la retirada de efectivo de una cuenta corriente desde la ventanilla de empleados.
     * Permite a un empleado extraer capital en nombre de un cliente tras realizar las pertinentes 
     * comprobaciones de saldo y estado de cuenta en la capa de negocio.
     * @param numeroCuenta Código IBAN de la cuenta corriente desde la que se extrae el dinero.
     * @param importe Cantidad de dinero en efectivo a retirar.
     * @throws CuentaNoEncontradaException Si el IBAN introducido no existe en el sistema.
     * @throws CuentaInactivaException Si la cuenta se encuentra bloqueada por incidencias o seguridad.
     * @throws SaldoInsuficienteException  Si el importe solicitado supera el capital disponible en la cuenta.
     * @throws PersistenciaException Si se produce un fallo de escritura durante la transacción en la BD.
     */
    public void retirarEnVentanilla(String numeroCuenta, double importe) throws CuentaNoEncontradaException, SaldoInsuficienteException, PersistenciaException {
        cuentaService.retirarDinero(numeroCuenta, importe);
    }

    /**
     * Devuelve la lista completa de todos los usuarios registrados (Para Empleados).
     * @return Lista de usuarios del sistema.
     * @throws PersistenciaException Si falla la lectura en la tabla.
     */
    public List<Usuario> obtenerTodosLosUsuarios() throws PersistenciaException {
        return usuarioDao.listarTodos();
    }

    /**
     * Registra un nuevo cliente aplicando polimorfismo, audita la acción a través del servicio
     * y le abre su primera cuenta bancaria de forma simultánea.
     * @param dni DNI único del cliente.
     * @param nombre Nombre completo.
     * @param email Correo de acceso.
     * @param password Contraseña.
     * @param telefono Teléfono para operativas como Bizum.
     * @param iban Código IBAN de la nueva cuenta.
     * @param saldoInicial Depósito de apertura en ventanilla.
     * @throws PersistenciaException Si hay un error al guardar en la base de datos.
     * @throws UsuarioYaExisteException Si el DNI ya pertenece a un cliente del banco.
     */
    public void registrarClientePorEmpleado(String dni, String nombre, String email, String password, String telefono, String iban, double saldoInicial) throws PersistenciaException, UsuarioYaExisteException {

    	if (usuarioDao.buscarNumeroCuentaPorDni(dni) != null) {
    		throw new UsuarioYaExisteException("El DNI " + dni + " ya está registrado en el sistema.");
    	}

    	Usuario nuevoUsuario = new Cliente();
    	nuevoUsuario.setDni(dni);
    	nuevoUsuario.setNombre(nombre);
    	nuevoUsuario.setEmail(email);
    	nuevoUsuario.setTelefono(telefono);
    	nuevoUsuario.setRol(Rol.CLIENTE);
    	nuevoUsuario.setPais("España"); 
    	nuevoUsuario.setDireccion("No especificada en ventanilla");
    	nuevoUsuario.setPasswordHash(password); 
    	nuevoUsuario.setPassword(password);


    	int ultimoId = usuarioDao.obtenerUltimoIdInterno();
    	nuevoUsuario.setId(ultimoId + 1);

    	String dniEmpleado = this.usuarioActivo.getDni();
    	empleadoService.registrarNuevoCliente(nuevoUsuario, dniEmpleado);

    	Cuenta nuevaCuenta = new Cuenta();
    	nuevaCuenta.setTitular(dni); 
    	nuevaCuenta.setNumeroCuenta(iban);
    	nuevaCuenta.setSaldo(saldoInicial);
    	nuevaCuenta.setEstadoCuenta(true);
    	nuevaCuenta.setFechaRegistro(new java.util.Date());

    	cuentaDao.insertarCuenta(nuevaCuenta, 0);
    }
    
    /**
     * Devuelve el historial unificado de todas las operaciones del banco (Para Empleados).
     * @return Lista de arrays de String con los datos de cada movimiento.
     * @throws PersistenciaException Si falla la consulta de auditoría.
     */
    public List<String[]> obtenerAuditoriaBanco() throws PersistenciaException {
        return operacionDao.obtenerHistorialGlobal();
    }

    /**
     * Devuelve el historial de movimientos en texto del usuario actual.
     * * @return Lista de cadenas con los movimientos formateados.
     * @throws PersistenciaException Si hay un fallo al consultar los datos.
     */
    public List<String> verMovimientos() throws PersistenciaException {
        return userService.obtenerMovimientosUsuario();
    }
    
    /**
     * Permite enviar un Bizum entre 2 usuarios del sistema.
     * * @param telefonoDestino Número de teléfono del usuario que recibirá el dinero.
     * @param importe Cantidad de dinero en euros (€) que se desea transferir.
     * @throws CuentaNoEncontradaException Si el teléfono de origen o de destino no están 
     * asociados a ninguna cuenta activa en el sistema.
     * @throws LimiteExcedidoException Si el importe no cumple con los límites mínimos 
     * o máximos establecidos por el banco para Bizum.
     * @throws DatoInvalidoException Si el cliente intenta realizar un Bizum a sí mismo 
     * o si los datos introducidos no son correctos.
     * @throws SaldoInsuficienteException  Si la cuenta de origen no dispone de fondos suficientes 
     * para cubrir el importe de la operación.
     * @throws PersistenciaException Si ocurre un fallo de integridad o error de conexión 
     * con la base de datos MySQL.
     */
    public void enviarBizum(String telefonoDestino, double importe) throws CuentaNoEncontradaException, LimiteExcedidoException, DatoInvalidoException, SaldoInsuficienteException, PersistenciaException {
        
        String telefonoOrigen = this.usuarioActivo.getTelefono();
        
        if (telefonoOrigen.trim().equals(telefonoDestino.trim())) {
            throw new DatoInvalidoException("Operación denegada: No puedes realizar un Bizum a tu propio número de teléfono.");
        }
        
        cuentaService.realizarBizum(telefonoOrigen, telefonoDestino, importe);
    }
    
    /**
     * Permite a un empleado cambiar el estado operativo (Bloquear/Activar) de una cuenta.
     * @param iban El IBAN de la cuenta a modificar.
     * @param nuevoEstado true para habilitar, false para bloquear.
     * @throws CuentaNoEncontradaException Si el IBAN no existe en la BD.
     * @throws PersistenciaException Si ocurre un error con MySQL.
     */
    public void cambiarEstadoCuenta(String iban, boolean nuevoEstado) throws CuentaNoEncontradaException, PersistenciaException {
        cuentaDao.actualizarEstadoCuenta(iban, nuevoEstado);
    }

    /**
     * Registra una nueva cuenta bancaria en el sistema vinculada al DNI de un titular.
     * @param nuevaCuenta Objeto cuenta con los datos de alta.
     * @throws PersistenciaException Si ocurre un fallo en el INSERT de MySQL.
     */
    public void crearNuevaCuenta(Cuenta nuevaCuenta) throws PersistenciaException {
        cuentaDao.insertarCuenta(nuevaCuenta, 0); 
    }
    
    /**
     * Permite a un empleado dar de baja o eliminar una cuenta bancaria del sistema usando su IBAN.
     * @param iban Número de cuenta que se desea eliminar.
     * @throws CuentaNoEncontradaException Si el IBAN introducido no existe en el sistema.
     * @throws PersistenciaException Si ocurre un error al ejecutar el borrado en MySQL.
     */
    public void eliminarUsuarioPorEmpleado(String iban, String dni) throws CuentaNoEncontradaException, PersistenciaException {
        cuentaDao.consultarSaldo(iban); 
        cuentaDao.eliminar(iban);
        usuarioDao.eliminarUsuario(dni);
    }

    /**
     * Obtiene el usuario que tiene la sesión activa en el sistema.
     * * @return El usuario logueado o null si no hay sesión.
     */
    public Usuario getUsuarioActivo() {
        return this.usuarioActivo;
    }
}