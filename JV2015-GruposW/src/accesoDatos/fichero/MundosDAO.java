package accesoDatos.fichero;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Mundo utilizando un ArrayList y un Hashtable
 *  persistentes en ficheros.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.1
 *  @source: MundosDAO.java 
 *  @version: 1.2 - 2016/06/05 
 *  @author: ajp
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import accesoDatos.db4o.Conexion;
import config.Configuracion;
import modelo.Mundo;
import modelo.Patron;
import modelo.Posicion;

public class MundosDAO implements OperacionesDAO, Persistente {

	// Requerido por el patrón Singleton
	private static MundosDAO instancia;

	// Elementos de almacenamiento.
	private static ArrayList<Mundo> datosMundos;
	private static File fMundos;

	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 * @throws DatosException 
	 */
	private MundosDAO() throws DatosException {
		datosMundos = new ArrayList<Mundo>();
		fMundos = new File(Configuracion.get().getProperty("mundos.nombreFichero"));
		recuperarDatos();
	}

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static MundosDAO getInstancia() {
		if (instancia == null) {
			try {
				instancia = new MundosDAO();
			} catch (DatosException e) {
				// No hay datos.
				cargarPedeterminados();
			}
		}
		return instancia;
	}

	/**
	 *  Método para generar de datos predeterminados.
	 */
	private static void cargarPedeterminados() {
		// En este array los 0 indican celdas con célula muerta y los 1 vivas
		byte[][] espacioDemo =  new byte[][]{ 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0 }, //
			{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0 }, // 
			{ 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0 }, // Given:
			{ 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 1x Planeador
			{ 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 1x Flip-Flop
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }  // 1x Still Life
		};
		Mundo mundoDemo = new Mundo("Demo0", new ArrayList<Integer>(), 
									new Hashtable<Patron,Posicion>(), espacioDemo);
		datosMundos.add(mundoDemo);
		guardarDatos(datosMundos);
	}

	//OPERACIONES DE PERSISTENCIA.
	/**
	 *  Recupera el Arraylist datosMundos almacenados en fichero. 
	 * @throws DatosException 
	 */
	@Override
	public void recuperarDatos() throws DatosException {
		try {
			if (fMundos.exists()) {
				FileInputStream fisMundos = new FileInputStream(fMundos);
				ObjectInputStream oisMundos = new ObjectInputStream(fisMundos);
				datosMundos = (ArrayList<Mundo>) oisMundos.readObject();
				oisMundos.close();
			}
			else {
				throw new DatosException("El fichero de datos no existe...");
			}
		} 
		catch (ClassNotFoundException e) {} 
		catch (IOException e) {}
	}
	
	/**
	 *  Cierra datos.
	 */
	@Override
	public void cerrar() {
		guardarDatos();
	}
	
	/**
	 *  Guarda el Arraylist de mundos en fichero.
	 */
	@Override
	public void guardarDatos() {
		guardarDatos(datosMundos);
	}
	
	/**
	 *  Guarda la lista recibida en el fichero de datos.
	 */
	private static void guardarDatos(List<Mundo> listaMundos) {
		try {
			FileOutputStream fosMundos = new FileOutputStream(fMundos);
			ObjectOutputStream oosSesiones = new ObjectOutputStream(fosMundos);
			oosSesiones.writeObject(listaMundos);		
			oosSesiones.flush();
			oosSesiones.close();
		} 
		catch (IOException e) {}	
	}

	//OPERACIONES DAO
	/**
	 * Obtiene el objeto dado el id utilizado para el almacenamiento.
	 * @param id - el mundo de Mundo a obtener.
	 * @return - el Mundo encontrado; null si no existe.
	 */	
	@Override
	public Mundo obtener(String nombreMundo) {
		assert nombreMundo != null;
		int comparacion;
		int inicio = 0;
		int fin = datosMundos.size() - 1;
		int medio;
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;
			comparacion = datosMundos.get(medio).getNombre()
					.compareToIgnoreCase(nombreMundo);
			if (comparacion == 0) {
				return datosMundos.get(medio);
			}
			if (comparacion < 0) {
				inicio = medio + 1;
			}
			else {
				fin = medio - 1;
			}
		}
		return null;	
	}

	/**
	 * Búsqueda de Usuario dado un objeto, reenvía al método que utiliza nombre.
	 * @param obj - el Mundo a buscar.
	 * @return - el Mundo encontrado; null si no existe.
	 */
	@Override
	public Mundo obtener(Object obj)  {
		return this.obtener(((Mundo) obj).getNombre());
	}
	
	/**
	 *  Alta de un objeto en el almacén de datos, 
	 *  sin repeticiones, según el campo id previsto. 
	 *	@param obj - Objeto a almacenar.
	 *  @throws DatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		Mundo mundo = (Mundo) obj; 
		int comparacion;
		int inicio = 0;
		int fin = datosMundos.size() - 1;
		int medio = 0;
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;			// Calcula posición central.
			// compara los dos id. Obtiene < 0 si id va después que medio.
			comparacion = datosMundos.get(medio).getNombre()
					.compareToIgnoreCase(mundo.getNombre());
			if (comparacion == 0) {			
				throw new DatosException("ALTA: El Mundo ya existe...");   				  
			}		
			if (comparacion < 0) {
				inicio = medio + 1;
			}			
			else {
				fin = medio - 1;
			}
		}	
		datosMundos.add(inicio, mundo); 	// Inserta el mundo en orden.		
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param nombre - el nombre del Mundo a eliminar.
	 * @return - el Mundo eliminado.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public Mundo baja(String nombre) throws DatosException {
		Mundo mundo = obtener(nombre);
		if (mundo != null) {
			// Elimina el Mundo del almacen de datos.
			datosMundos.remove(mundo);
		}	
		else {
			throw new DatosException("BAJA: El Mundo no existe...");
		}
		return mundo;
	}

	/**
	 *  Actualiza datos de un Mundo reemplazando el almacenado por el recibido.
	 *	@param obj - Mundo con las modificaciones.
	 *  @throws DatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException {
		Mundo mundo = (Mundo) obj;
		Mundo mundoAux = obtener(mundo.getNombre());
		if (mundoAux != null) {	
			mundoAux.setDistribucion(mundo.getDistribucion());
			mundoAux.setEspacio(mundo.getEspacio());
			mundoAux.setConstantes(mundo.getConstantes());
			
			// Actualización
			datosMundos.set(datosMundos.indexOf(mundo), mundoAux);
		}	
		else {
			throw new DatosException("ACTUALIZAR: El Mundo no existe...");
		}
	}

	/**
	 * Obtiene el listado de todos los objetos Mundo almacenados.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		for (Mundo mundo: datosMundos) {
			if (mundo != null) {
				listado.append("\n" + mundo);
			}
		}
		return listado.toString();
	}

} // class