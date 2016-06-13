package modelo;
/** 
 * Proyecto: Juego de la vida.
 *  Implementa el concepto de Patron de celdas según el modelo2.1
 *  @since: prototipo2.1
 *  @source: Patron.java 
 *  @version: 1.0 - 20/04/2016
 *  @author: ajp
 */
import java.io.Serializable;
import java.util.Arrays;

public class Patron implements Serializable {
	
	//Atributos
	private String nombre;
	private byte[][] esquema;
	
	/**
	 * Constructor convencional.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros que se corresponden con los atributos.
	 * Utiliza métodos set... para la posible verificación.
	 * @param nombre
	 * @param esquema
	 */
	public Patron(String nombre, byte[][] esquema) {
		setNombre(nombre);
		setEsquema(esquema);
	}

	/**
	 * Constructor por defecto.
	 * Establece el valor inicial, por defecto, de cada uno de los atributos.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Patron() {
		this("NombrePatron", new byte[1][1]);
	}
	
	/**
	 * Constructor copia.
	 * Establece el valor inicial de cada uno de los atributos a partir de
	 * los valores obtenidos de un objeto de su misma clase.
	 * El atributo esquema es clonado utilizando utilidades de clonación de arrays.
	 * @param p
	 */
	public Patron(Patron p) {
		this(p.nombre, p.esquema);
		this.esquema = new byte[p.esquema.length][p.esquema.length];	
		for (int i=0; i <p.esquema.length; i++) {
			this.esquema[i] = Arrays.copyOf(p.esquema[i], p.esquema[i].length);
		}                                
	}
	
	 /** Constructor especial.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros para inicializa los atributos.
	 * Utiliza métodos set... para la posible verificación.
	 * @param nombre
	 * @param filas
	 * @param columnas
	 * @param imagenPatron
	 */
	public Patron(String nombre, int filas, int columnas, String imagenPatron) {
		setNombre(nombre);
		
		byte [][] esquema = new byte [filas][columnas];
		//...
		setEsquema(esquema);
	}
	
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return the esquema
	 */
	public byte[][] getEsquema() {
		return esquema;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		if (nombre != null) {
			this.nombre = nombre;
		}
		else {
			this.nombre = "NombrePatron";
		}
	}

	/**
	 * @param esquema the esquema to set
	 */
	public void setEsquema(byte[][] esquema) {
		
		if (esquema == null || esquema.length == 0) {
			byte[][] aux = {{0}};
			this.esquema  = aux;
		}		
		else {
			this.esquema = esquema;
		}
	}
	
	@Override
	public String toString() {
		return String.format("Patron [nombre=%s, esquema=%s]", nombre,
				Arrays.toString(esquema));
	}
	
} // class
