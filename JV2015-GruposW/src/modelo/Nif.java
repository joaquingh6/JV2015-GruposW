package modelo;
/** Proyecto: Juego de la vida.
 *  Implementa el concepto de Nif según el modelo2.  
 *  @since: prototipo2.0
 *  @source: Nif.java 
 *  @version: 1.0 - 14/03/2016 
 *  @author: ajp
 */
import java.io.Serializable;

import util.Formato;

public class Nif implements Serializable { 
	
	// Atributo
	private String texto;

	// Constructores
	/**
	 * Constructor convencional.
	 * Establece el valor inicial del atributo.
	 * Recibe un parámetro que se corresponden con el atributo
	 */
	public Nif(String texto) {
		setTexto(texto);
	}

	/**
	 * Constructor por defecto.
	 */
	public Nif() {
		this("00000000I");
	}

	/**
	 * Constructor copia.
	 */
	public Nif(Nif nif) {
		this(nif.texto);
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		assert nifValido(texto);
		this.texto = texto;
	}
	
	/**
	 * Comprueba la validez de un nif.
	 * @param texto.
	 * @return true si cumple.
	 */
	private boolean nifValido(String texto) {		
		if (texto != null
				&& util.Formato.validar(texto, Formato.PATRON_NIF)
				&& letraValida(texto)) {
			return true;
		}
		return false;		
	}
	
	/**
	 * Comprueba la letra del un nif.
	 * @param nif.
	 * @return true si es correcta.
	 */
	private boolean letraValida(String textoNif) {
		// Algoritmo de obtención letra
		//--Pendiente--
		return true;
	}

	@Override
	public String toString() {
		return texto;
	}
	
} //class
