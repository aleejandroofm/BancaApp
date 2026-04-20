package Modelo;

public class Cliente extends Usuario {
	
	private String idCliente;

	public Cliente(String nombre, String telefono, String pais, int id, String dni, String rol, String email, String direccion, String password, String passwordHash, String idCliente, boolean autenticado) {
        super(nombre, telefono, pais, id, dni, rol, email, direccion, password, passwordHash, autenticado); 
        this.idCliente = idCliente;
    }

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	
	public void solicitarPrestamo() {
        
    }

    public void mostrarCuentas() {
        
    }
    
    
	
	

}
