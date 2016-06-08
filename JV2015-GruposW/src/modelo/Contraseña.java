package modelo;

import java.io.Serializable;

import config.Configuracion;
import util.Formato;

public class Contraseña implements Serializable {

	private String texto;
	
	public Contraseña(String texto) {
		setTexto(texto);
	}

	public Contraseña() {
		this(Configuracion.get().getProperty("usuario.passwordPredeterminada"));
	}

	public Contraseña(Contraseña contraseña) {
		this(contraseña.texto);
	}

	public void setTexto(String texto) {
		assert esValida(texto);
		this.texto = encriptar(texto);
	}
	
	/**
	 * Comprueba validez de una contraseña.
	 * @param texto.
	 * @return true si cumple.
	 */
	public static boolean esValida(String texto) {
		if (texto != null 
				&& util.Formato.validar(texto, Formato.PATRON_CONTRASEÑA3)) {
			return true;
		}
		return false;
	}

	/**
	 * Encripta una texto utilizando una clase de utilidad.
	 * @param claveAcceso - a encriptar.
	 * @return clave encriptada.
	 */
	private String encriptar(String claveAcceso) {	
		return util.Criptografia.cesar(claveAcceso);
	}
	
	@Override
	public String toString() {
		return texto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() == obj.getClass()) {
			if (texto.equals(((Contraseña)obj).texto)) {
				return true;
			}
		}
		return false;
	}

} // class
