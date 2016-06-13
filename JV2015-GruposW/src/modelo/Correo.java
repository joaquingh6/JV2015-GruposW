package modelo;
/** Proyecto: Juego de la vida.
 *  Implementa el concepto de Correo según el modelo2.  
 *  @since: prototipo2.0
 *  @source: Correo.java 
 *  @version: 1.0 - 14/03/2016 
 *  @author: ajp
 */
import java.io.Serializable;

import util.Formato;

public class Correo implements Serializable{
	
	private String texto;
	
	public Correo(String texto) {
		setTexto(texto);
	}

	public Correo() {
		this("correo@correo.com");
	}

	public Correo(Correo correo) {
		this(correo.texto);
	}
	
	public void setTexto(String texto) {
		assert esValido(texto);
		this.texto = texto;
	}
	
	/**
	 * Comprueba validez de una dirección de correo.
	 * @param texto.
	 * @return true si cumple.
	 */
	public static boolean esValido(String texto) {
		if (texto != null 
				&& util.Formato.validar(texto, Formato.PATRON_CORREO)
				&& correoAutentico(texto)) {
			return true;
		}
		return false;
	}

	/**
	 * Comprueba que una dirección de correo existe.
	 * @return true si cumple.
	 */
	private static boolean correoAutentico(String texto) {
		// Comprueba que el correo no es falso
		// --Pendiente--
		return true;
	}

	public String getTexto() {
		return texto;
	}
	
	@Override
	public String toString() {
		return texto;
	}
	
} // class
