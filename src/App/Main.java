package App;

import Controlador.Controlador;
import Vista.LoginView;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Controlador ct = new Controlador();
		LoginView login = new LoginView(ct);
		login.mostrarLogin();
	}
	
}
