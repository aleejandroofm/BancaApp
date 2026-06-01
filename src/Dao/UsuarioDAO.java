package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Modelo.Usuario;
import Conexion.ConexionDB;
import Excepciones.Sistema.PersistenciaException;

import java.util.ArrayList;
import java.util.List;
import Modelo.Cliente;
import Modelo.Empleado;
import Modelo.Administrador;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UsuarioDAO {
    
    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    
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
            user.setRol(rol);
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

    public boolean guardar(Usuario user) {
        String sql = "INSERT INTO usuario (nombre, dni, rol, direccion, telefono, password, pais, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getDni());
            ps.setString(3, user.getRol());
            ps.setString(4, user.getDireccion());
            ps.setString(5, user.getTelefono());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPais());
            ps.setString(8, user.getEmail());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en guardar(): " + e.getMessage(), e);
            throw new PersistenciaException("Error de persistencia al intentar registrar el nuevo usuario.", e);
        }
    }
    
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
    
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_interno = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error en eliminar(): " + e.getMessage(), e);
            throw new PersistenciaException("No se ha podido eliminar el registro del usuario por un error en el servidor.", e);
        }
    }

    public Usuario login(String email, String password) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND password = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);
            
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                } 
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante el proceso de login en BD: " + e.getMessage(), e);
            throw new PersistenciaException("Error crítico en el sistema al procesar la autenticacion.", e);
        }
        return null;        
    }
}