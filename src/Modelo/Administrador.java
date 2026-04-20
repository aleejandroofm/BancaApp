package Modelo;

public class Administrador extends Usuario {
	
	private String idAdministrador;
	
	public Administrador(String nombre, String telefono, String pais, int id, String dni, String rol, String email, String direccion, String password, String passwordHash, String idAdministrador, boolean autenticado) {
        super(nombre, telefono, pais, id, dni, rol, email, direccion, password, passwordHash, autenticado);
        this.idAdministrador = idAdministrador;
    }
	
	public void crearUsuario() {
		// Lógica
	}
	
	
    public void editarUsuario() {
    	// Lógica
    }
    
    
    public void eliminarUsuario() {
    	// Lógica
    }
    
    
    public void configurarPermisos() {
    	// Lógica
    }

    
	public String getIdAdministrador() {
		return idAdministrador;
	}

	public void setIdAdministrador(String idAdministrador) {
		this.idAdministrador = idAdministrador;
	}
	
	
}
