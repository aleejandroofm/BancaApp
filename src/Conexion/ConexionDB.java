package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	private static final String URL = "jdbc:mysql://localhost:3306/banco";
	private static final String USUARIO = "root";
	private static final String PASSWORD = "";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USUARIO, PASSWORD);
	}
	
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexion: " + e.getMessage());
			}
		}
	}
}
