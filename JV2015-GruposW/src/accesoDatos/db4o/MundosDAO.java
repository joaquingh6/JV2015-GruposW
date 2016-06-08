package accesoDatos.db4o;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Mundo utilizando base de datos db4o.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.2
 *  @source: MundosDAO.java 
 *  @version: 1.1 - 2016/06/02 
 *  @author: ajp
 */
import modelo.Mundo;
import modelo.Patron;
import modelo.Posicion;
import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class MundosDAO implements OperacionesDAO {

	// Requerido por el Singleton 
	private static MundosDAO instancia = null;

	// Elementos de almacenamiento.
	// Base datos db4o
	private ObjectContainer db;

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static MundosDAO getInstancia() {
		if (instancia == null) {
			instancia = new MundosDAO();
		}
		return instancia;
	}
	
	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private MundosDAO() {
		db = Conexion.getDB();
		if (obtener("Demo0") == null) {
			cargarPedeterminados();
		}
	}

	/**
	 *  Método para generar de datos predeterminados.
	 */
	private void cargarPedeterminados() {
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
		try {
			alta(mundoDemo);
		} catch (DatosException e) {}
	}

	/**
	 *  Cierra conexión.
	 */
	@Override
	public void cerrar() {
		Conexion.cerrarConexiones();
	}
	
	//OPERACIONES DAO
	/**
	 * Obtiene objeto Mundo dado su nombre.
	 * @param nombreMundo - el nombre de Mundo a buscar.
	 * @return - el Mundo encontrado; null si no existe.
	 */	
	@Override
	public Mundo obtener(String nombreMundo) {
		Query consulta = db.query();
		consulta.constrain(Mundo.class);
		consulta.descend("nombre").equals(nombreMundo);
		ObjectSet<Mundo> result = consulta.execute();

		if (result.size() > 0) {
			return result.get(0);
		}
		else {
			return null;
		}
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
	 *  Alta de un nuevo Mundo sin repeticiones según el campo nombre. 
	 *  @param obj - Mundo a buscar y obtener.
	 *  @throws AccesoDatosException - si ya existe.
	 */	
	@Override
	public void alta(Object obj) throws DatosException {
		Mundo mundo = (Mundo) obj;
		if (obtener(mundo.getNombre()) == null) {
			// Almacena el Mundo en la base de datos
			db.store(mundo);
		}	
		else
			throw new DatosException("ALTA: El Mundo ya existe...");
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param nombreMundo - el nombre del Mundo a eliminar.
	 * @return - el Mundo eliminado.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public Mundo baja(String nombreMundo) throws DatosException {
		Mundo mundo = obtener(nombreMundo);
		if (mundo != null) {
			// Elimina el Mundo del almacen de datos.
			db.delete(mundo);
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
			//mundoAux.setNombre(mundoAux.getNombre());
			mundoAux.setDistribucion(mundo.getDistribucion());
			mundoAux.setEspacio(mundo.getEspacio());
			mundoAux.setConstantes(mundo.getConstantes());	
			// Actualización
			db.store(mundoAux);
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
		Query consulta = db.query();
		consulta.constrain(Mundo.class);
		ObjectSet<Mundo> result = consulta.execute();
		for (Mundo mundo: result) {
			if (mundo != null) {
				listado.append("\n" + mundo);
			}
		}
		return listado.toString();
	}

} //class
