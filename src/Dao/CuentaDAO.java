package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Conexion.ConexionDB;
import Modelo.Cuenta;
import Excepciones.Cuenta.CuentaNoEncontradaException;
import Excepciones.Sistema.PersistenciaException;

/**
 * Clase DAO que gestiona el acceso a datos para la entidad Cuenta (capa de Datos).
 * Se encarga de realizar las operaciones CRUD directamente en la base de datos
 * utilizando JDBC estándar y consultas SQL preparadas.
 */
public class CuentaDAO  {
    
    private static final Logger logger = Logger.getLogger(CuentaDAO.class.getName());
    
    /**
     * Inserta una nueva cuenta bancaria vinculada a un cliente en la base de datos.
     * Mapea los atributos del objeto Cuenta y transforma la fecha de registro a formato SQL.
     * @param cuenta El objeto Cuenta con todos los datos que se van a guardar.
     * @param idUsuario El identificador único del usuario propietario (para futuras referencias).
     * @throws PersistenciaException Si ocurre un fallo de conexión o un error de sintaxis en SQL.
     */
    public void insertarCuenta(Cuenta cuenta, int idUsuario) {
        String sql = "INSERT INTO cuenta (dni_titular, numeroCuenta, saldo, fechaRegistro, estadoCuenta) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cuenta.getTitular());
            ps.setString(2, cuenta.getNumeroCuenta());
            ps.setDouble(3, cuenta.getSaldo());
            ps.setDate(4, new java.sql.Date(cuenta.getFechaRegistro().getTime()));
            ps.setBoolean(5, cuenta.isEstadoCuenta());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar cuenta: " + e.getMessage(), e);
            throw new PersistenciaException("Error al registrar la cuenta en la base de datos.", e);
        } 
    }
    
    /**
     * Recupera una lista con los números de cuenta (IBAN) vinculados a un mismo DNI.
     * @param dni El documento de identidad del titular que queremos consultar.
     * @return Una lista de cadenas de texto (List de String) con los IBAN encontrados.
     * @throws PersistenciaException Si la consulta select falla en el servidor.
     */
    public List<String> listarCuentasPorDni(String dni) {
        List<String> cuentas = new ArrayList<>();
        String sql = "SELECT numeroCuenta FROM cuenta WHERE dni_titular = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cuentas.add(rs.getString("numeroCuenta"));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar las cuentas del usuario", e);
        }
        return cuentas;
    }
    
    /**
     * Busca la información completa de una cuenta utilizando su número (IBAN).
     * Mapea la fila devuelta por la base de datos transformándola en un objeto del modelo.
     * @param numeroCuenta El IBAN de la cuenta que queremos localizar.
     * @return Un objeto de tipo Cuenta relleno con sus respectivos atributos cargados.
     * @throws CuentaNoEncontradaException Si la búsqueda no devuelve ninguna fila de datos.
     * @throws PersistenciaException Si surge algun fallo crítico en el servidor de base de datos.
     */
    public Cuenta buscarPorNumero(String numeroCuenta) {
        String sql = "SELECT * FROM cuenta WHERE numeroCuenta = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, numeroCuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cuenta c = new Cuenta();
                    c.setTitular(rs.getString("dni_titular")); 
                    c.setNumeroCuenta(rs.getString("numeroCuenta")); 
                    c.setSaldo(rs.getDouble("saldo"));
                    c.setFechaRegistro(rs.getDate("fechaRegistro"));
                    c.setEstadoCuenta(rs.getBoolean("estadoCuenta"));
                    return c;
                } else {
                    throw new CuentaNoEncontradaException("La cuenta con el número " + numeroCuenta + " no existe");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar cuenta: " + e.getMessage(), e);
            throw new PersistenciaException("Error al consultar la cuenta en el sistema de almacenamiento.", e);
        }
    }
    
    /**
     * Busca y devuelve el primer número de cuenta (IBAN) que encuentre asociado al DNI de un cliente.
     * @param dniTitular El DNI del cliente del que queremos obtener la cuenta activa.
     * @return El número de cuenta en formato String si existe, o null si el titular no tiene cuentas.
     * @throws PersistenciaException Si ocurre un error de comunicación al realizar la consulta.
     */
    public String buscarNumeroCuentaPorDni(String dniTitular) {
        String sql = "SELECT numeroCuenta FROM cuenta WHERE dni_titular = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dniTitular);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numeroCuenta");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar cuenta por DNI: " + e.getMessage(), e);
            throw new PersistenciaException("Error al buscar el número de cuenta asociado al cliente.", e);
        }
        return null;
    }
    
    /**
     * Obtiene el IBAN de una cuenta bancaria realizando un JOIN entre la tabla cuenta y usuario,
     * utilizando como criterio el número de teléfono del propietario.
     * @param telefono El número de teléfono móvil registrado por el usuario.
     * @return El IBAN de la cuenta asociada para la operativa, o null si no se localiza.
     * @throws PersistenciaException Si la consulta relacional con JOIN da algún fallo en la BD.
     */
    public String obtenerIbanPorTelefono(String telefono) {
        String sql = "SELECT c.numeroCuenta FROM cuenta c JOIN usuario u ON c.dni_titular = u.dni WHERE u.telefono = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, telefono);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numeroCuenta");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener IBAN por teléfono: " + e.getMessage(), e);
            throw new PersistenciaException("Error al procesar la búsqueda por número de teléfono.", e);
        }
        return null;
    }
    
    /**
     * Consulta de forma directa el saldo disponible de una cuenta.
     * @param numeroCuenta El IBAN de la cuenta bancaria a consultar.
     * @return El saldo actual en formato Double.
     * @throws CuentaNoEncontradaException Si la cuenta introducida no existe en el sistema.
     * @throws PersistenciaException Si hay problemas de red o conexión al leer la columna saldo.
     */
    public Double consultarSaldo(String numeroCuenta) {
        String sql = "SELECT saldo FROM cuenta WHERE numeroCuenta = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, numeroCuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                } else {
                    throw new CuentaNoEncontradaException("La cuenta con el IBAN " + numeroCuenta + " no existe.");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al consultar saldo: " + e.getMessage(), e);
            throw new PersistenciaException("Error al obtener el saldo de la cuenta desde el servidor.", e);
        }
    }
    
    /**
     * Modifica el saldo actual de una cuenta en la base de datos aplicando un bloque UPDATE.
     * Comprueba también que la base de datos registre de forma efectiva la modificación de la fila.
     * @param numeroCuenta El IBAN de la cuenta bancaria que va a ser modificada.
     * @param nuevoSaldo El importe total final calculado que queremos guardar en el registro.
     * @throws CuentaNoEncontradaException Si el UPDATE devuelve 0 filas afectadas porque el IBAN no existe.
     * @throws PersistenciaException Si el motor de la base de datos rechaza el comando UPDATE.
     */
    public void actualizarSaldo(String numeroCuenta, Double nuevoSaldo) {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numeroCuenta = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, numeroCuenta);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new CuentaNoEncontradaException("No se ha podido encontrar la cuenta para actualizar su saldo.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar saldo: " + e.getMessage(), e);
            throw new PersistenciaException("No se pudo persistir la actualización del saldo de la cuenta.", e);
        }
    }
    
    /**
     * Elimina por completo una cuenta bancaria del registro del sistema de almacenamiento.
     * @param numeroCuenta El IBAN exacto de la cuenta que se pretende borrar.
     * @throws CuentaNoEncontradaException Si no existe ninguna cuenta en la BD que coincida con el IBAN.
     * @throws PersistenciaException Si el borrado afecta a alguna clave foránea (FK) en el historial.
     */
    public void eliminar(String numeroCuenta) {
        String sql = "DELETE FROM cuenta WHERE numeroCuenta = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, numeroCuenta);
            int filas = ps.executeUpdate();
            
            if (filas == 0) {
                throw new CuentaNoEncontradaException("No existe la cuenta que se intenta eliminar.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar cuenta: " + e.getMessage(), e);
            throw new PersistenciaException("Error de integridad al intentar eliminar la cuenta de la base de datos.", e);
        }
    }
}