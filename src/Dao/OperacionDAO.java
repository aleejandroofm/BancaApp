package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Conexion.ConexionDB;
import Excepciones.Sistema.PersistenciaException;
import Modelo.Operacion;
import Modelo.TipoOperacion;

/**
 * Clase DAO que gestiona el acceso a datos para la tabla Operacion.
 * Registra movimientos monetarios y trazas de auditoría administrativa.
 * @author Alejandro Ferrándiz Martínez
 */
public class OperacionDAO {
    
    private static final Logger logger = Logger.getLogger(OperacionDAO.class.getName());
    
    /**
     * Registra una transacción monetaria (Transferencia, Efectivo, Bizum) en la base de datos.
     * @param op El objeto Operacion con los importes y cuentas.
     * @param tipoOperacion Tipo de movimiento.
     * @throws PersistenciaException Si la inserción infringe alguna restricción de la BD.
     */
    public void registrarOperacion(Operacion op, TipoOperacion tipoOperacion) throws PersistenciaException {
        String sql = "INSERT INTO operacion (tipoOperacion, importe, idCuentaOrigen, idCuentaDestino, fechaOperacion) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, tipoOperacion.name());
            ps.setDouble(2, op.getImporte());
            
            if (op.getIdCuentaOrigen() == null || op.getIdCuentaOrigen().trim().isEmpty()) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, op.getIdCuentaOrigen());
            }
            
            if (op.getIdCuentaDestino() == null || op.getIdCuentaDestino().trim().isEmpty()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, op.getIdCuentaDestino());
            }
            
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Operación rechazada por la BD: " + e.getMessage());
            throw new PersistenciaException("Operación rechazada por el sistema bancario: " + e.getMessage(), e);
        }
    }

    /**
     * Registra una acción de auditoría administrativa realizada por un empleado (Altas, Bloqueos).
     * @param detalle Descripción de la acción administrativa.
     * @param tipo Tipo de evento administrativo (ALTA_USUARIO, ESTADO_CUENTA).
     * @throws PersistenciaException Si falla el volcado en MySQL.
     */
    public void registrarAccionAdministrativa(String detalle, TipoOperacion tipo) throws PersistenciaException {
        String sql = "INSERT INTO operacion (tipoOperacion, importe, idCuentaOrigen, idCuentaDestino, fechaOperacion) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, tipo.name());
            ps.setDouble(2, 0.0); 
            ps.setString(3, "EMPLEADO");
            ps.setString(4, detalle); 
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al registrar la auditoría: " + e.getMessage());
            throw new PersistenciaException("Error crítico al guardar la auditoría.", e);
        }
    }

    /**
     * Recupera el historial de movimientos de un número de cuenta específico.
     * @param numeroCuenta Código IBAN de la cuenta a buscar.
     * @return Lista de cadenas de texto formateadas.
     * @throws PersistenciaException Si falla la consulta SELECT.
     */
    public List<String> obtenerMovimientosPorCuenta(String numeroCuenta) throws PersistenciaException {
        List<String> movimientos = new ArrayList<>();
        String sql = "SELECT tipoOperacion, importe, idCuentaOrigen, idCuentaDestino, fechaOperacion " +
                     "FROM operacion WHERE idCuentaOrigen = ? OR idCuentaDestino = ? " +
                     "ORDER BY fechaOperacion DESC";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, numeroCuenta);
            ps.setString(2, numeroCuenta);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipoOperacion");
                    double importe = rs.getDouble("importe");
                    String origen = rs.getString("idCuentaOrigen");
                    String destino = rs.getString("idCuentaDestino");
                    Timestamp fecha = rs.getTimestamp("fechaOperacion");
                
                    String signo = "+";
                    if (numeroCuenta.equals(origen)) {
                        signo = "-";
                    }

                    String linea = String.format("[%s] %s: %s%.2f € (Origen: %s | Destino: %s)", 
                            fecha.toString().substring(0, 16), tipo, signo, importe, 
                            (origen == null ? "Efectivo" : origen), 
                            (destino == null ? "Efectivo" : destino));
                    
                    movimientos.add(linea);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al obtener movimientos: " + e.getMessage());
            throw new PersistenciaException("Error al cargar el historial de movimientos desde la base de datos.", e);
        }
        return movimientos;
    }
    
    /**
     * Recupera el histórico integral unificado de operaciones del banco (Para Empleados).
     * @return Colección de arrays con todas las filas de la tabla.
     * @throws PersistenciaException Si ocurre un fallo en el servidor relacional.
     */
    public List<String[]> obtenerHistorialGlobal() throws PersistenciaException {
        List<String[]> historial = new ArrayList<>();
        String sql = "SELECT idOperacion, tipoOperacion, importe, idCuentaOrigen, idCuentaDestino, fechaOperacion FROM operacion ORDER BY fechaOperacion DESC";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                historial.add(new String[]{
                    String.valueOf(rs.getInt("idOperacion")),
                    rs.getString("tipoOperacion"),
                    String.valueOf(rs.getDouble("importe")),
                    rs.getString("idCuentaOrigen") != null ? rs.getString("idCuentaOrigen") : "CAJERO",
                    rs.getString("idCuentaDestino") != null ? rs.getString("idCuentaDestino") : "CAJERO",
                    rs.getTimestamp("fechaOperacion").toString()
                });
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al recuperar el histórico unificado de operaciones bancarias.", e);
        }
        return historial;
    }
}