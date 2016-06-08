package accesoDatos.db4o;

/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO SimulacionesDAO utilizando base de datos db4o.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.2
 *  @source: SimulacionesDAO.java 
 *  @version: 1.1 - 2016/06/02 
 *  @author: ajp
 */
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import accesoDatos.fichero.MundosDAO;
import modelo.Mundo;
import modelo.Simulacion;
import modelo.Usuario;
import modelo.Simulacion.EstadoSimulacion;
import util.Fecha;

public class SimulacionesDAO implements OperacionesDAO {

	// Requerido por el Singleton 
	private static SimulacionesDAO instancia = null;
	
	// Elemento de almacenamiento.
	// Base datos db4o
	private ObjectContainer db;

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static SimulacionesDAO getInstancia() {
		if (instancia == null) {
			instancia = new SimulacionesDAO();
		}
		return instancia;
	}
	
	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private SimulacionesDAO() {
		db = Conexion.getDB();
		if (obtenerTodasMismoUsr("III0I") == null) {
			cargarPredeterminados();
		}
	}
	
	/**
	 *  Método para generar de datos predeterminados.
	 */
	private void cargarPredeterminados() {
		// Obtiene usuario y mundo predeterminados.
		Mundo mundoDemo = MundosDAO.getInstancia().obtener("Demo0");
		Simulacion simulacionDemo = new Simulacion(new Usuario(), new Fecha(), mundoDemo, EstadoSimulacion.PREPARADA);
		alta(simulacionDemo);
	}
	
	/**
	 *  Cierra conexión.
	 */
	@Override
	public void cerrar() {
		Conexion.cerrarConexiones();
	}
	
	//Operaciones DAO
	
	/**
	 * Búsqueda binaria de Simulacion dado idUsr y fecha.
	 * @param idSimulacion - el idUsr+fecha de la Simulacion a buscar. 
	 * @return - la Simulacion encontrada; null si no existe.
	 */	
	public Simulacion obtener(String idSimulacion) {	
		ObjectSet<Simulacion> result = null;
		Query consulta = db.query();
		consulta.constrain(Simulacion.class);
		consulta.descend("getIdSimulacion()").equals(idSimulacion);
		result = consulta.execute();
		if (result.size() > 0) {
			return result.get(0);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Búsqueda de Sesion dado un objeto, reenvía al método que utiliza idSesion.
	 * @param obj - la Simulacion a buscar.
	 * @return - la Simulacion encontrada; null si no existe.
	 */
	@Override
	public Simulacion obtener(Object obj)  {
		return this.obtener(((Simulacion) obj).getIdSimulacion());
	}
	
	/**
	 * Obtiene de todas las simulaciones por IdUsr de usuario.
	 * @param idUsr - el idUsr a buscar.
	 * @return - las simulaciones encontradas o null si no existe.
	 */
	public List<Simulacion> obtenerTodasMismoUsr(String idUsr) {
		ObjectSet<Simulacion> result = null;
		Query consulta = db.query();
		consulta.constrain(Simulacion.class);
		consulta.descend("usr").equals(idUsr);
		result = consulta.execute();
		if (result.size() > 0) {
			return (List<Simulacion>) result;
		}
		else {
			return null;
		}
	}
	
	/**
	 *  Alta de una nueva Simulacion en orden y sin repeticiones según los idUsr más fecha. 
	 *  Busca previamente la posición que le corresponde por búsqueda binaria.
	 *  @param obj - Simulación a almacenar.
	 *  @throws AccesoDatosException - si ya existe.
	 */	
	public void alta(Object obj) {
		Simulacion simulacion = (Simulacion) obj;
		//actualiza datos
		db.store(simulacion);	
	}

	@Override
	public Simulacion baja(String id) throws DatosException {	
		Simulacion simulacion = obtener(id);
		if (simulacion != null){
			db.delete(simulacion);
		}
		else {
			throw new DatosException("BAJA: La simulación no existe...");
		}
		return simulacion;
	}

	@Override
	public void actualizar(Object obj) throws DatosException {
		Simulacion simulacion = (Simulacion) obj;
		Simulacion simulacionAux = obtener(simulacion.getIdSimulacion());
		if(simulacionAux != null) {
			simulacionAux.setUsr(simulacion.getUsr());
			simulacionAux.setFecha(simulacion.getFecha());
			simulacionAux.setMundo(simulacion.getMundo());
			simulacionAux.setEstado(simulacion.getEstado());
			db.store(simulacionAux);
		}
		else {
			throw new DatosException("ACTUALIZAR: La Simulacion no existe...");
		}
	}

	/**
	 * Obtiene el listado de todos las simulaciones almacenadas.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		ObjectSet<Simulacion> result = null;
		Query consulta = db.query();
		consulta.constrain(Simulacion.class);	
		result = consulta.execute();
		if (result.size() > 0) {
			for (Simulacion s: result) {
				if (s != null) {
					listado.append("\n" + s);
				}
			}
			return listado.toString();
		}
		else {
			return null;
		}
	}
	
} //class
