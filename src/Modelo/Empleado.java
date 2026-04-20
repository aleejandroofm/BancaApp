package Modelo;

public class Empleado extends Usuario {
	
	private double salario;
    private String tipoPuesto;
    private String idEmpleado;
    
    public Empleado(String nombre, String telefono, String pais, int id, String dni, String rol, String email, String direccion, String password, String passwordHash, double salario, String tipoPuesto, String idEmpleado, boolean autenticado) {
        super(nombre, telefono, pais, id, dni, rol, email, direccion, password, passwordHash, autenticado);
        this.salario = salario;
        this.tipoPuesto = tipoPuesto;
        this.idEmpleado = idEmpleado;
    }
    
    public void verificarIdentidad() {
    	// Lógica
    }

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public String getTipoPuesto() {
		return tipoPuesto;
	}

	public void setTipoPuesto(String tipoPuesto) {
		this.tipoPuesto = tipoPuesto;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
    
    

}
