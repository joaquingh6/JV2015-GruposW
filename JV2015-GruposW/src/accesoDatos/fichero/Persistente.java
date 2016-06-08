package accesoDatos.fichero;
/** 
 * Proyecto: Juego de la vida.
 *  Interfaz con las operaciones básicas de persistencia en fichero.
 *  @since: prototipo2.2
 *  @source: Persistente.java  
 * @version: 1.0 - 2015/05/28 
 *  @author: ajp
 */
import accesoDatos.DatosException;

public interface Persistente {

	/**
	 *  Permite guarda el Arraylist de objetos en ficheros.
	 */
	void guardarDatos();
	
	/**
	 *  Permite recupera el Arraylist de objetos almacenados en fichero. 
	 * @throws DatosException 
	 */
	void recuperarDatos() throws DatosException;

} // interface
