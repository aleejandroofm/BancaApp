package Modelo;


public abstract class Usuario implements Autenticable {
	private String nombre;
    private String telefono;
    private String pais;
    private int id;
    private String dni;
    private String rol;
    private String email;
    private String direccion;
    private String password;
    private String passwordHash;
    private Cuenta cuenta;
    private boolean autenticado = false;

    
    public Usuario() {
    	
    	this.setCuenta(new Cuenta());
    	
    }

    public Usuario(String nombre, String telefono, String pais, int id, String dni, String rol, String email, String direccion, String password, String passwordHash) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.pais = pais;
        this.id = id;
        this.dni = dni;
        this.rol = rol;
        this.email = email;
        this.direccion = direccion;
        this.password = password;
        this.passwordHash = passwordHash;
    }

    
    public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}

	public boolean login(String credenciales) {
		if (credenciales != null && credenciales.equals(password)) {
			this.autenticado = true;
			return true;
		} else {
			this.autenticado = false;
			return false;
		}
    }
	


    public boolean logout() {
    	if (this.autenticado = true) {
    		this.autenticado = false;
    		return true;
    	}
    	
    	return false;
       
    }

	

}
