package modelo;
/** 
 * Proyecto: Juego de la vida.
 *  Es un punto del espacio donde se ubica un Patron de celulas, 
 *  según el modelo2.1
 *  @since: prototipo2.1
 *  @source: Posicion.java 
 *  @version: 1.0 - 24/04/2016
 *  @author: ajp
 */

import java.io.Serializable;

public class Posicion implements Serializable {

	//Atributos
	private int x;
	private int y;

	/**
	 * Constructor convencional.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros que se corresponden con los atributos.
	 * Utiliza métodos set... para la posible verificación.
	 * @param x
	 * @param y
	 */
	public Posicion(int x, int y) {
		setX(x);
		setY(y);
	}
	
	/**
	 * Constructor por defecto.
	 * Establece el valor inicial, por defecto, de cada uno de los atributos.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Posicion() {
		this(0, 0);
	}
	
	/**
	 * Constructor copia.
	 * Establece el valor inicial de cada uno de los atributos a partir de
	 * los valores obtenidos de un objeto de su misma clase.
	 * @param p
	 */
	public Posicion(Posicion p) {
		this(p.x, p.y);
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

} //class

