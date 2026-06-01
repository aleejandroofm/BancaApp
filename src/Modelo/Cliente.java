package Modelo;

public class Cliente extends Usuario {
	
	private String idCliente;
	
	public Cliente() {
		super();
	}

	public Cliente(String nombre, String telefono, String pais, int id, String dni, String rol, String email, String direccion, String password, String passwordHash, String idCliente) {
        super(nombre, telefono, pais, id, dni, rol, email, direccion, password, passwordHash); 
        this.idCliente = idCliente;
    }

	// Getters y Setters
	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
}