package Conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Excepciones.Sistema.ConfiguracionException;
import Excepciones.Sistema.PersistenciaException;

/**
 * Clase de utilidad encargada de gestionar la conexión con la base de datos (capa de Datos).
 * Utiliza un archivo de propiedades externo para cargar las credenciales y la URL de la BD,
 * aislando la configuración del código fuente.
 * @author Alejandro Ferrándiz Martínez
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
                // 🛑 LANZAMOS CONFIGURACIONEXCEPTION: Bloquea el arranque si no hay archivo de configuración
                throw new ConfiguracionException("Error de arranque: El archivo 'config.properties' no se encuentra en la ruta del proyecto.");
            } else {
                props.load(input);
                LOGGER.info("Configuración cargada correctamente desde config.properties");
            }
        } catch (ConfiguracionException e) {
            throw e;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al leer el fichero de configuración", e);
            throw new ConfiguracionException("Error crítico: No se ha podido procesar o leer el archivo 'config.properties'.", e);
        }
    }

    /**
     * Establece una conexión con la base de datos.
     * En lugar de capturar el error y devolver null, envuelve
     * el fallo en una excepción personalizada de persistencia.
     * @return Connection objeto de conexión activa listo para usarse.
     * @throws PersistenciaException Si el servidor de BD está caído o las credenciales son incorrectas.
     */
    public static Connection conectar() throws PersistenciaException {
        try {
            String url = props.getProperty("db.url");
            return DriverManager.getConnection(url, props);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
            // 🟢 Mantenemos tu PersistenciaException pero adaptada a tu constructor con código de error
            throw new PersistenciaException("No se pudo establecer conexión con el servidor del banco.", e);        
        }
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