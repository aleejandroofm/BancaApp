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
 * DAO encargado de la gestión de persistencia de la entidad Cuenta.
 * Incluye operaciones CRUD, consultas de saldo y utilidades de búsqueda
 * por DNI o teléfono.
 */
public class CuentaDAO  {

    private static final Logger logger = Logger.getLogger(CuentaDAO.class.getName());

    /**
     * Actualiza el saldo de una cuenta dentro de una transacción existente.
     * @param con conexión activa a la base de datos.
     * @param numeroCuenta IBAN de la cuenta a actualizar.
     * @param nuevoSaldo nuevo saldo calculado.
     * @throws SQLException si ocurre un error en la ejecución SQL o no se encuentra la cuenta.
     */
    public void actualizarSaldoConTransaccion(Connection con, String numeroCuenta, double nuevoSaldo) throws SQLException {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numeroCuenta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, numeroCuenta);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo encontrar la cuenta para actualizar el saldo.");
            }
        }
    }

    /**
     * Inserta una nueva cuenta en la base de datos.
     * @param cuenta objeto Cuenta a persistir.
     * @param idUsuario identificador del usuario propietario.
     * @throws PersistenciaException si ocurre un error al guardar en la base de datos.
     */
    public void insertarCuenta(Cuenta cuenta, int idUsuario) throws PersistenciaException {
        String sql = "INSERT INTO cuenta (dni_titular, numeroCuenta, saldo, fechaRegistro, estadoCuenta) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cuenta.getTitular());
            ps.setString(2, cuenta.getNumeroCuenta());
            ps.setDouble(3, cuenta.getSaldo());
            ps.setDate(4, new Date(cuenta.getFechaRegistro().getTime()));
            ps.setBoolean(5, cuenta.isEstadoCuenta());

            ps.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al insertar cuenta en la BD: " + e.getMessage());
            throw new PersistenciaException("Error al registrar la cuenta en la base de datos.", e);
        }
    }

    /**
     * Lista los números de cuenta asociados a un DNI.
     * @param dni DNI del titular.
     * @return lista de números de cuenta asociados.
     * @throws PersistenciaException si ocurre un error en la consulta SQL.
     */
    public List<String> listarCuentasPorDni(String dni) throws PersistenciaException {
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
            logger.log(Level.WARNING, "Error al listar cuentas: " + e.getMessage());
            throw new PersistenciaException("Error al listar cuentas del usuario.", e);
        }

        return cuentas;
    }

    /**
     * Busca una cuenta por su número de cuenta.
     * @param numeroCuenta IBAN de la cuenta.
     * @return objeto Cuenta encontrado.
     * @throws CuentaNoEncontradaException si no existe la cuenta.
     * @throws PersistenciaException si ocurre un error en la base de datos.
     */
    public Cuenta buscarPorNumero(String numeroCuenta) throws CuentaNoEncontradaException, PersistenciaException {
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
                    throw new CuentaNoEncontradaException("No existe la cuenta: " + numeroCuenta, "ERR-CUENTA-404");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al buscar cuenta por IBAN: " + e.getMessage());
            throw new PersistenciaException("Error al consultar la cuenta en la base de datos.", e);
        }
    }

    /**
     * Busca el número de cuenta (IBAN) asociado al DNI de un titular.
     * @param dniTitular El DNI del cliente a consultar.
     * @return El número de cuenta (IBAN) si se localiza en el sistema.
     * @throws CuentaNoEncontradaException Si el DNI suministrado no posee ninguna cuenta vinculada.
     * @throws PersistenciaException Si ocurre un fallo relacional crítico al conectar con MySQL.
     */
    public String buscarNumeroCuentaPorDni(String dniTitular) throws CuentaNoEncontradaException, PersistenciaException {
        String sql = "SELECT numeroCuenta FROM cuenta WHERE dni_titular = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dniTitular);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numeroCuenta");
                } else {
                    throw new CuentaNoEncontradaException("No se localizó ninguna cuenta corriente vinculada al DNI: " + dniTitular, "ERR-CUENTA-404");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al buscar cuenta por DNI en la BD: " + e.getMessage());
            throw new PersistenciaException("Error interno en el servidor al recuperar los datos de la cuenta.", e);
        }
    }

    /**
     * Obtiene el IBAN asociado al teléfono del usuario para Bizum.
     * @param telefono teléfono del usuario.
     * @return número de cuenta asociado.
     * @throws CuentaNoEncontradaException si no hay ninguna cuenta asociada a ese terminal.
     * @throws PersistenciaException si ocurre un error en la consulta.
     */
    public String obtenerIbanPorTelefono(String telefono) throws CuentaNoEncontradaException, PersistenciaException {
        String sql = "SELECT c.numeroCuenta FROM cuenta c JOIN usuario u ON c.dni_titular = u.dni WHERE u.telefono = ? ";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, telefono);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numeroCuenta");
                } else {
                    throw new CuentaNoEncontradaException("Ningún usuario o IBAN asociado al número de teléfono: " + telefono, "ERR-BIZUM-404");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al obtener IBAN por teléfono: " + e.getMessage());
            throw new PersistenciaException("Error en búsqueda relacional por teléfono.", e);
        }
    }

    /**
     * Consulta el saldo de una cuenta.
     * @param numeroCuenta IBAN de la cuenta.
     * @return saldo actual.
     * @throws CuentaNoEncontradaException si la cuenta no existe.
     * @throws PersistenciaException si ocurre un error en la consulta.
     */
    public Double consultarSaldo(String numeroCuenta) throws CuentaNoEncontradaException, PersistenciaException {
        String sql = "SELECT saldo FROM cuenta WHERE numeroCuenta = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, numeroCuenta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                } else {
                    throw new CuentaNoEncontradaException("La cuenta consultada no existe.", "ERR-CUENTA-404");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al consultar saldo en la BD: " + e.getMessage());
            throw new PersistenciaException("Error al obtener el saldo desde el servidor.", e);
        }
    }

    /**
     * Actualiza el saldo de una cuenta.
     * @param numeroCuenta IBAN de la cuenta.
     * @param nuevoSaldo nuevo saldo a guardar.
     * @throws CuentaNoEncontradaException si la cuenta no existe.
     * @throws PersistenciaException si ocurre un error en la actualización.
     */
    public void actualizarSaldo(String numeroCuenta, Double nuevoSaldo) throws CuentaNoEncontradaException, PersistenciaException {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numeroCuenta = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, numeroCuenta);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new CuentaNoEncontradaException("No existe la cuenta a actualizar.", "ERR-CUENTA-404");
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al actualizar saldo en la BD: " + e.getMessage());
            throw new PersistenciaException("No se pudo actualizar el balance en el servidor.", e);
        }
    }
    
    /**
     * Modifica el estado operativo de una cuenta bancaria (Habilitada / Bloqueada) en la base de datos.
     * @param numeroCuenta El IBAN de la cuenta que se va a modificar.
     * @param nuevoEstado true para activar / habilitar la cuenta, false para inactivar / bloquearla.
     * @throws CuentaNoEncontradaException Si el IBAN introducido no existe en el sistema.
     * @throws PersistenciaException Si ocurre un error crítico de comunicación con MySQL.
     */
    public void actualizarEstadoCuenta(String numeroCuenta, boolean nuevoEstado) throws CuentaNoEncontradaException, PersistenciaException {
        String sql = "UPDATE cuenta SET estadoCuenta = ? WHERE numeroCuenta = ?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setBoolean(1, nuevoEstado);
            ps.setString(2, numeroCuenta);
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CuentaNoEncontradaException("No se ha encontrado ninguna cuenta registrada con el IBAN: " + numeroCuenta, "ERR-CUENTA-404");
            }
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al actualizar estado de la cuenta: " + e.getMessage());
            throw new PersistenciaException("Error crítico en la base de datos al intentar cambiar el estado.", e);
        }
    }

    /**
     * Elimina una cuenta de la base de datos.
     * @param numeroCuenta IBAN de la cuenta a eliminar.
     * @throws CuentaNoEncontradaException si la cuenta no existe.
     * @throws PersistenciaException si ocurre un error en la eliminación.
     */
    public void eliminar(String numeroCuenta) throws CuentaNoEncontradaException, PersistenciaException {
        String sql = "DELETE FROM cuenta WHERE numeroCuenta = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, numeroCuenta);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new CuentaNoEncontradaException("No existe la cuenta a eliminar.", "ERR-CUENTA-404");
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al eliminar cuenta en la BD: " + e.getMessage());
            throw new PersistenciaException("Error eliminando la cuenta por restricciones del servidor.", e);
        }
    }
}	
