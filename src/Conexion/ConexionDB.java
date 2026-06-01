package Conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de utilidad encargada de gestionar la conexión con la base de datos (capa de Datos).
 * Utiliza un archivo de propiedades externo para cargar las credenciales y la URL de la BD,
 * aislando la configuración del código fuente.
 */
public class ConexionDB {
    
    private static final Logger LOGGER = Logger.getLogger(ConexionDB.class.getName());
    private static final Properties props = new Properties();
    
    // Bloque estático que se ejecuta una sola vez al cargar la clase en memoria.
    // Se encarga de buscar y leer el archivo config.properties del src/classpath.
    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOGGER.severe("No se pudo encontrar el archivo config.properties en src");
            } else {
                props.load(input);
                LOGGER.info("Configuración cargada correctamente desde config.properties");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al leer el fichero de configuración", e);
        }
    }

    /**
     * Establece y devuelve una conexión activa con el servidor de la base de datos.
     * Utiliza la URL y las credenciales que se cargaron previamente desde el fichero de propiedades.
     * @return El objeto Connection listo para realizar consultas SQL, o null si la conexión falla.
     */
    public static Connection conectar() {
        Connection conexion = null;

        try {
            String url = props.getProperty("db.url");
            conexion = DriverManager.getConnection(url, props);
            LOGGER.info("Conexión establecida con éxito.");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
        }
        return conexion;
 
    }
    
    /**
     * Cierra de forma segura una conexión abierta con la base de datos si esta no es nula.
     * Libera los recursos del servidor para evitar saturar el pool de conexiones de la BD.
     * @param con El objeto Connection que se desea liberar y cerrar.
     */
    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar la conexión", e);
            }
        }
    }
    
}