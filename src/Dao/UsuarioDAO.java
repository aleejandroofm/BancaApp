package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Modelo.Usuario;
import Conexion.ConexionDB;
import Excepciones.Autenticacion.UsuarioBloqueadoException;
import Excepciones.Sistema.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import Modelo.Cliente;
import Modelo.Empleado;
import Modelo.Rol;
import Modelo.Administrador;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas
 * con los usuarios del sistema.
 * Permite realizar búsquedas, altas, modificaciones, eliminaciones,
 * autenticación y consultas auxiliares sobre la información almacenada
 * en la base de datos.
 */
public class UsuarioDAO {
    
    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    
    /**
     * Busca un usuario a partir de su identificador interno.
     * @param idInterno identificador único del usuario en la base de datos.
     * @return el usuario encontrado o {@code null} si no existe.
     * @throws PersistenciaException si ocurre un error durante la consulta.
     */
    public Usuario buscarPorID(int idInterno) {
        String sql = "SELECT * FROM usuario WHERE id_interno = ?";
        Usuario user = null;
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idInterno);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapearUsuario(rs);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al buscar por ID (" + idInterno + "): " + e.getMessage(), e);
            throw new PersistenciaException("Error al consultar el usuario en el sistema de almacenamiento.", e);
        }
        
        return user;
    }
    
    /**
     * Convierte una fila del ResultSet en una instancia de
     * Usuario o de una de sus clases derivadas.
     * @param rs conjunto de resultados posicionado sobre una fila válida.
     * @return objeto usuario mapeado con los datos obtenidos.
     * @throws Exception si se produce algún error durante el mapeo.
     */
    private Usuario mapearUsuario(ResultSet rs) throws Exception {
        Usuario user = null;
        String rol = rs.getString("rol");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String telefono = rs.getString("telefono");
        String pais = rs.getString("pais");
        int idInterno = rs.getInt("id_interno");
        String email = rs.getString("email");
        String direccion = rs.getString("direccion");
        String password = rs.getString("password");
        String passwordHash = rs.getString("passwordHash");
        
        if ("CLIENTE".equalsIgnoreCase(rol)) {
            Cliente c = new Cliente();
            user = c;
        } else if ("EMPLEADO".equalsIgnoreCase(rol)) {
            Empleado e = new Empleado();
            user = e;
        } else if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
            Administrador a = new Administrador();
            user = a;
        }
        
        if (user != null) {
            user.setNombre(nombre);
            user.setDni(dni);
            user.setRol(Rol.valueOf(rs.getString("rol").toUpperCase().trim()));
            user.setId(idInterno);
            user.setDireccion(direccion);
            user.setTelefono(telefono);
            user.setPassword(password);
            user.setPasswordHash(passwordHash);
            user.setAutenticado(false);
            user.setPais(pais);
            user.setEmail(email);
        }
        
        return user;
    }
    
   /**
     * Busca el ID más alto de la tabla de usuarios para poder generar el siguiente de forma consecutiva.
     * Si la tabla se encuentra vacía, se devuelve un valor base por defecto para iniciar la secuencia.
     * @return el identificador numérico más alto encontrado en la base de datos, 
     * o 100 como valor inicial si no existen registros.
     * @throws PersistenciaException si ocurre un error de acceso a datos o una anomalía en la consulta SQL.
     */
    public int obtenerUltimoIdInterno() throws PersistenciaException {
        String sql = "SELECT MAX(id_interno) FROM usuario";
        try (Connection con = ConexionDB.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                // Si la tabla estuviera vacía, empezamos en el 100, si no, devolvemos el más alto
                return (maxId == 0) ? 100 : maxId; 
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al calcular el nuevo ID de usuario: " + e.getMessage());
        }
        return 100;
    }
    
    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     * @return lista con todos los usuarios almacenados.
     * @throws PersistenciaException si ocurre un error durante la consulta.
     */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        
        try (Connection con = ConexionDB.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario user = mapearUsuario(rs);
                if (user != null) {
                    lista.add(user);
                }
            }
                
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en listarTodos(): " + e.getMessage(), e);
            throw new PersistenciaException("Error al obtener la lista completa de usuarios.", e);
        }
        
        return lista; 
    }
    
    /**
     * Busca el número de cuenta asociado a un DNI.
     * @param dniTitular DNI del titular de la cuenta.
     * @return número de cuenta asociado o {@code null} si no existe.
     * @throws PersistenciaException si ocurre un error de acceso a datos.
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
            throw new PersistenciaException("Error al buscar la cuenta asociada al DNI.", e);
        }
        return null;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     * @param user usuario que se desea almacenar.
     * @return true si la operación se realizó correctamente, false en caso contrario.
     * @throws PersistenciaException si ocurre un error durante la inserción.
     */
    public boolean guardar(Usuario user) {
        String sql = "INSERT INTO usuario (id_interno, nombre, dni, rol, direccion, telefono, password, passwordHash, pais, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, user.getId());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getDni());
            ps.setString(4, user.getRol().name());
            ps.setString(5, user.getDireccion());
            ps.setString(6, user.getTelefono());
            ps.setString(7, user.getPassword());
            ps.setString(8, user.getPasswordHash());
            ps.setString(9, user.getPais());
            ps.setString(10, user.getEmail());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en guardar(): " + e.getMessage(), e);
            throw new PersistenciaException("Error de persistencia al intentar registrar el nuevo usuario.", e);
        }
    }
    
    /**
     * Actualiza los datos modificables de un usuario existente.
     * @param user usuario con la información actualizada.
     * @return true si se actualizó al menos un registro / false en caso contrario.
     * @throws PersistenciaException si ocurre un error durante la actualización.
     */
    public boolean actualizar(Usuario user) {
        String sql = "UPDATE usuario SET nombre = ?, direccion = ?, telefono = ?, pais = ? WHERE id_interno = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getDireccion());
            ps.setString(3, user.getTelefono());
            ps.setString(4, user.getPais());
            ps.setInt(5, user.getId());
            
            return ps.executeUpdate() > 0;
        
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en actualizar(): " + e.getMessage(), e);
            throw new PersistenciaException("Error al actualizar los datos del usuario en el sistema.", e);
        }
    }
    
    /**
     * Elimina un usuario de la base de datos utilizando su DNI.
     * @param dni DNI del usuario a eliminar.
     * @return true si el usuario fue eliminado correctamente; false en caso contrario.
     * @throws PersistenciaException si ocurre un error durante la eliminación.
     */
    public boolean eliminarUsuario(String dni) throws PersistenciaException {
        String sql = "DELETE FROM usuario WHERE dni = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en eliminarPorDni(): " + e.getMessage(), e);
            throw new PersistenciaException("No se ha podido eliminar el registro del usuario por un error en el servidor.", e);
        }
    }

    /**
     * Autentica un usuario mediante su correo electrónico y contraseña.
     * @param email correo electrónico del usuario.
     * @param password contraseña asociada al usuario.
     * @return el usuario autenticado si las credenciales son válidas, null en caso contrario.
     * @throws UsuarioBloqueadoException si el usuario está inactivo/bloqueado.
     * @throws PersistenciaException si ocurre un error durante el proceso de autenticación.
     */
    public Usuario login(String email, String password) throws UsuarioBloqueadoException, PersistenciaException {
    	
        String sql = "SELECT * FROM usuario WHERE email = ? AND password = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);
            
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean activo = rs.getBoolean("activo");
                    if (!activo) {
                        throw new UsuarioBloqueadoException("Acceso denegado: Tu cuenta de usuario se encuentra desactivada por seguridad.");
                    }
                    
                    return mapearUsuario(rs);
                } 
            }
            
        } catch (UsuarioBloqueadoException e) {
            throw e;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante el proceso de login en BD: " + e.getMessage(), e);
            throw new PersistenciaException("Error crítico en el sistema al procesar la autenticacion.", e);
        }
        
        return null;        
    }
}
