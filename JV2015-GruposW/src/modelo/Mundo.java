package modelo;
/** 
 * Proyecto: Juego de la vida.
 *  Representa el espacio y las leyes que determinan el universo 
 *  de simulación del según el modelo2.1
 *  @since: prototipo2.1
 *  @source: Mundo.java 
 *  @version: 1.0 - 24/04/2016
 *  @author: ajp
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Mundo implements Leyes, Serializable {

	private final static int TAMAÑO = 12;
	private String nombre;
	private List<Integer> constantes;
	private Map<Patron, Posicion> distribucion;
	private byte[][] espacio;	

	/**
	 * Constructor convencional.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros que se corresponden con los atributos.
	 * Utiliza métodos set... para la posible verificación.
	 * @param nombre
	 * @param constantes
	 * @param distribucion
	 * @param espacio
	 */
	public Mundo(String nombre, List<Integer> constantes, 
			Map<Patron, Posicion> distribucion, byte[][] espacio) {
		setNombre(nombre);
		setConstantes(constantes);
		setDistribucion(distribucion);
		setEspacio(espacio);
	}

	/**
	 * Constructor por defecto.
	 * Establece el valor inicial, por defecto, de cada uno de los atributos.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Mundo() {
		this("MundoDefecto", new ArrayList<Integer>(), 
				new Hashtable<Patron, Posicion>(), new byte[TAMAÑO][TAMAÑO]);
	}

	/**
	 * Constructor copia.
	 * Establece el valor inicial de cada uno de los atributos a partir de
	 * los valores obtenidos de un objeto de su misma clase.
	 * Llama al constructor convencional utilizando objetos obtenidos
	 * con los contructores copia de los atributos.
	 * El atributo espacio es clonado utilizando utilidades de clonación de arrays.
	 * @param mundo - el Mundo a clonar
	 */
	public Mundo(Mundo mundo) {
		this(mundo.nombre, new ArrayList<Integer>(mundo.constantes), 
				new Hashtable<Patron,Posicion>(mundo.distribucion), mundo.espacio);
		
		this.espacio = new byte[mundo.espacio.length][mundo.espacio.length];
		
		for (int i=0; i <mundo.espacio.length; i++)
			this.espacio[i] = Arrays.copyOf(mundo.espacio[i], mundo.espacio[i].length);
			//System.arraycopy(m.espacio[i], 0, this.espacio[i], 0, m.espacio[i].length);	
	}
	
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return the constantes
	 */
	public List<Integer> getConstantes() {
		return constantes;
	}

	/**
	 * @return the distribucion
	 */
	public Map<Patron, Posicion> getDistribucion() {
		return distribucion;
	}
	
	/**
	 * @return the espacio
	 */
	public byte[][] getEspacio() {
		return espacio;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		if (nombre == null) {
			this.nombre = "MundoDefecto";
		}
		else {
			this.nombre = nombre; 
		}
	}

	/**
	 * @param constantes the constantes to set
	 */
	public void setConstantes(List<Integer> parametros) {

		if (parametros == null) {
			this.constantes = new ArrayList<Integer>();
		}
		else {
			this.constantes = parametros;
		}
	}

	/**
	 * @param distribucion the distribucion to set
	 */
	public void setDistribucion(Map<Patron, Posicion> distribucion) {
		this.distribucion = distribucion;
	}

	/**
	 * @param espacio the espacio to set
	 */
	public void setEspacio(byte[][] espacio) {
		if (espacio == null || espacio.length == 0) {
			this.espacio = new byte[TAMAÑO][TAMAÑO];
			for (int i=0; i < espacio.length; i++) {
				for (int j=0; j < espacio.length; j++) {
					espacio[i][j] = 0; 
				}
			}
		}
		else {
			this.espacio = espacio;
		}
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Mundo [nombre=%s, parametros=%s, distribucion=%s, espacio=%s]",
						nombre, constantes, distribucion,
						Arrays.toString(espacio));
	}

	//Métodos de la interface Leyes
	/**
	 * Establece la manera en que actualiza el estado de las células del mundo: 
	 * responde a la regla: El tiempo transcurre y se producen cambios...
	 */
	@Override
	public Object actualizarEstado()  {     					
		int tam = espacio.length; 
		byte[][] nuevaRealidad = new byte[tam][tam];

		for (int i = 0; i < tam; i++) {
			for (int j = 0; j <= 11; j++) {
				int vecinas = 0;					//células adyacentes
				// las celdas situadas fuera del mundo, con índices fuera de rango, darán
				// ArrayIndexOutOfBoundsException y son equivalentes a células vacías a efectos de 
				// vecindad.
				try {
					vecinas += espacio[i-1][j];	    //celda N			NO | N | NE
					vecinas += espacio[i-1][j+1];	//celda NE			----------- 
					vecinas += espacio[i][j+1];	    //celda E			 O |   | E
					vecinas += espacio[i+1][j+1];	//celda SE          -----------
					vecinas += espacio[i+1][j]; 	//celda S           SO | S | SE
					vecinas += espacio[i+1][j-1];    //celda SO 
					vecinas += espacio[i][j-1];	    //celda O           			                                     	
					vecinas += espacio[i-1][j-1]; 	//celda NO
				} 
				catch (java.lang.ArrayIndexOutOfBoundsException e) { } 

				if (vecinas < 2) {
					nuevaRealidad[i][j] = 0; 				// subpoblación, muere
				}
				if (vecinas > 3) {
					nuevaRealidad[i][j] = 0; 				// sobrepoblación, muere
				}
				if (vecinas == 3) {
					nuevaRealidad[i][j] = 1; 				// pasa a estar viva o se mantiene
				}
				if (vecinas == 2 && espacio[i][j] == 1) {						
					nuevaRealidad[i][j] = 1; 				// se mantiene viva
				}
			}
		}
		espacio = nuevaRealidad;
		return this;
	}
	
} //class
