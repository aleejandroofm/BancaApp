package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Conexion.ConexionDB;
import Excepciones.Sistema.PersistenciaException;
import Modelo.Operacion;

/**
 * Clase DAO que gestiona el acceso a datos para la tabla Operacion (capa de Datos).
 * Registra en la base de datos cualquier movimiento bancario (Efectivo, Bizum, Transferencia)
 * y recupera el historial de transacciones formateado para el usuario.
 */
public class OperacionDAO {
	
	private static final Logger logger = Logger.getLogger(OperacionDAO.class.getName());
	
	/**
	 * Registra una nueva transacción en la base de datos
	 * Extrae los datos comunes de la operación y controla si las cuentas de origen o destino
	 * son nulas (en caso de ingresos/retiradas en efectivo) aplicando Types.VARCHAR.
	 * @param op El objeto Operacion
	 * @param tipoOperacion Cadena de texto con el tipo de movimiento (Efectivo, Bizum, etc.).
	 * @throws PersistenciaException Si la inserción SQL falla o hay problemas de conexión.
	 */
	public void registrarOperacion(Operacion op, String tipoOperacion) {
		
		String sql = "INSERT INTO operacion (tipoOperacion, importe, idCuentaOrigen, idCuentaDestino, fechaOperacion) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection con = ConexionDB.conectar();
		     PreparedStatement ps = con.prepareStatement(sql)) {
			
			ps.setString(1, tipoOperacion);
			ps.setDouble(2, op.getImporte());
			
			// Si no hay cuenta origen, metemos un NULL en la BD
			if (op.getIdCuentaOrigen() == null || op.getIdCuentaOrigen().trim().isEmpty()) {
				ps.setNull(3, Types.VARCHAR);
			} else {
				ps.setString(3, op.getIdCuentaOrigen());
			}
			
			// Si no hay cuenta destino, metemos un NULL en la BD
			if (op.getIdCuentaDestino() == null || op.getIdCuentaDestino().trim().isEmpty()) {
				ps.setNull(4, Types.VARCHAR);
			} else {
				ps.setString(4, op.getIdCuentaDestino());
			}
			
			// Capturamos la fecha y hora exacta del sistema en este instante
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error al registrar la operación: " + e.getMessage(), e);
			throw new PersistenciaException("No se pudo registrar el movimiento en el historial bancario.", e);
		}
	}

	/**
	 * Recupera todo el historial de movimientos de un número de cuenta (ya sea como origen o destino).
	 * Devuelve los registros ordenados por fecha de la más nueva a la más antigua y genera
	 * líneas de texto formateadas con signos (+/-) según gane o pierda dinero la cuenta.
	 * @param numeroCuenta El IBAN de la cuenta de la que queremos extraer el historial.
	 * @return Una lista de cadenas (List de String) listas para ser impresas por pantalla.
	 * @throws PersistenciaException Si la consulta SELECT falla al comunicarse con MySQL.
	 */
	public List<String> obtenerMovimientosPorCuenta(String numeroCuenta) {
		
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
					
					// Si la cuenta analizada coincide con el origen, significa que de ahí SALE dinero (-)
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
			logger.log(Level.SEVERE, "Error al obtener movimientos en OperacionDAO: " + e.getMessage(), e);
			throw new PersistenciaException("Error al cargar el historial de movimientos desde la base de datos.", e);
		}
		return movimientos;
	}
}