package Modelo;

public class Administrador extends Usuario {
	
	private String idAdministrador;
	
	// Constructor vacío reglamentario
	public Administrador() {
		super();
	}
	
	public Administrador(String nombre, String telefono, String pais, int id, String dni, String rol, String email, String direccion, String password, String passwordHash, String idAdministrador) {
        super(nombre, telefono, pais, id, dni, rol, email, direccion, password, passwordHash);
        this.idAdministrador = idAdministrador;
    }
	
	// Getters y Setters
	public String getIdAdministrador() {
		return idAdministrador;
	}

	public void setIdAdministrador(String idAdministrador) {
		this.idAdministrador = idAdministrador;
	}
}