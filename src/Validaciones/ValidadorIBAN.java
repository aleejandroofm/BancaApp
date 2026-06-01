package Validaciones;

/**
 * Clase que se encarga de validar los números de cuenta bancaria (IBAN).
 * Contiene métodos estáticos para comprobar que el formato sea correcto y que
 * no se hagan operaciones inválidas entre cuentas idénticas.
 */
public class ValidadorIBAN {
	
	/**
	 * Valida que el IBAN introducido cumpla con los requisitos mínimos de estructura.
	 * Pasa el texto a mayúsculas y comprueba que no esté vacío, que empiece por 'ES' 
	 * y que tenga exactamente 24 caracteres de longitud.
	 * @param iban El número de cuenta en formato texto que se va a comprobar.
	 * @throws IllegalArgumentException Si la cuenta es nula, no empieza por 'ES' o no mide 24 caracteres.
	 */
	public static void validarFormato(String iban) {
		
		if (iban == null || iban.trim().isEmpty()) {
			throw new IllegalArgumentException("El numero de cuenta no puede ser nulo.");
		}
		
		String ibanMayusculas = iban.toUpperCase().trim();
		
		if (!ibanMayusculas.startsWith("ES")) {
			throw new IllegalArgumentException("El IBAN debe comenzar con el formato de pais 'ES'.");
		}
		
		if (ibanMayusculas.length() != 24) {
			throw new IllegalArgumentException("El número de cuenta no tiene una longitud valida.");
		}
		
	}
	
	/**
	 * Comprueba que la cuenta de origen y la de destino no sean la misma.
	 * Sirve para evitar que un usuario intente hacerse una transferencia o un Bizum a sí mismo.
	 * @param cuentaOrigen El IBAN de la cuenta que envía el dinero.
	 * @param cuentaDestino El IBAN de la cuenta que recibe el dinero.
	 * @throws IllegalArgumentException Si ambos números de cuenta son iguales (sin importar mayúsculas o minúsculas).
	 */
	public static void validarCuentasDistintas(String cuentaOrigen, String cuentaDestino) {
		if (cuentaOrigen != null && cuentaOrigen.equalsIgnoreCase(cuentaDestino)) {
			throw new IllegalArgumentException("No puedes realizar una transferencia a tu propia cuenta.");
		}
	}
}