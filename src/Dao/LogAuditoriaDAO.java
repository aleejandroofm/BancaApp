package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Conexion.ConexionDB;
import Excepciones.Sistema.PersistenciaException;
import Modelo.LogAuditoria;

/**
 * Clase DAO encargada de la persistencia de la tabla LogAuditoria.
 * @author Alejandro Ferrándiz Martínez
 */
public class LogAuditoriaDAO {

    private static final Logger logger = Logger.getLogger(LogAuditoriaDAO.class.getName());

    /**
     * Inserta una nueva traza de auditoría utilizando los campos de tu tabla.
     * @param log Objeto con la información a registrar.
     * @throws PersistenciaException Si la inserción SQL falla.
     */
    public void registrarAccion(LogAuditoria log) throws PersistenciaException {
        String sql = "INSERT INTO LogAuditoria (accion, resultado, idUsuario) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, log.getAccion());
            ps.setBoolean(2, log.isResultado());
            ps.setString(3, log.getIdUsuario());

            ps.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al insertar log de auditoría: " + e.getMessage());
            throw new PersistenciaException("No se pudo registrar el evento en la BD: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera el histórico de auditoría de la base de datos.
     * @return Lista de objetos LogAuditoria.
     * @throws PersistenciaException Si falla la lectura.
     */
    public List<LogAuditoria> obtenerTodos() throws PersistenciaException {
        List<LogAuditoria> lista = new ArrayList<>();
        String sql = "SELECT idLog, accion, fechaAccion, resultado, idUsuario FROM LogAuditoria ORDER BY fechaAccion DESC";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new LogAuditoria(
                    rs.getInt("idLog"),
                    rs.getString("accion"),
                    rs.getTimestamp("fechaAccion"),
                    rs.getBoolean("resultado"),
                    rs.getString("idUsuario")
                ));
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al leer tabla LogAuditoria: " + e.getMessage());
            throw new PersistenciaException("Error al cargar el panel de control.", e);
        }
        return lista;
    }
}
