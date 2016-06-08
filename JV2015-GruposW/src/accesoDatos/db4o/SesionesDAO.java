package accesoDatos.db4o;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO SesionUsuario utilizando base de datos db4o.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.2
 *  @source: SesionesDAO.java 
 *  @version: 1.1 - 2016/06/02 
 *  @author: ajp
 */
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import accesoDatos.fichero.UsuariosDAO;
import modelo.SesionUsuario;
import modelo.Usuario;
import modelo.SesionUsuario.EstadoSesion;
import util.Fecha;

public class SesionesDAO implements OperacionesDAO {
	
	// Requerido por el Singleton 
	private static SesionesDAO instancia = null;

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
	public static SesionesDAO getInstancia() {
		if (instancia == null) {
			instancia = new SesionesDAO();
		}
		return instancia;
	}
	
	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private SesionesDAO() {
		db = Conexion.getDB();
		if (obtenerTodasMismoUsr("III0I") == null) {
			cargarPredeterminados();
		}
	}
	
	/**
	 *  Método para generar de datos predeterminados.
	 */
	private void cargarPredeterminados() {
		// Sesion invitado "III0I" "Miau#0".
		// Obtiene usuario predeterminado.
		Usuario usrDemo = UsuariosDAO.getInstancia().obtener("III0I");
		SesionUsuario sesionDemo = new SesionUsuario(usrDemo, new Fecha(), EstadoSesion.CERRADA);
		alta(sesionDemo);
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
	 * Búsqueda simple de sesión por idUsr + fecha.
	 * @param idSesion - la SesionUsuario a buscar.
	 * @return - la sesión encontrada; null si no existe.
	 */
	@Override
	public SesionUsuario obtener(String idSesion)  {
		ObjectSet<SesionUsuario> result = null;
		Query consulta = db.query();
		consulta.constrain(SesionUsuario.class);
		consulta.descend("getIdSesion()").equals(idSesion);
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
	 * @param obj - la SesionUsuario a buscar.
	 * @return - la Sesion encontrada; null si no existe.
	 */
	@Override
	public SesionUsuario obtener(Object obj)  {
		return this.obtener(((SesionUsuario) obj).getIdSesion());
	}	
	
	/**
	 * Obtiene de todas las sesiones por IdUsr de usuario.
	 * @param idUsr - el idUsr a buscar.
	 * @return - las sesiones encontradas o null si no existe.
	 */
	public List<SesionUsuario> obtenerTodasMismoUsr(String idUsr) {
		ObjectSet<SesionUsuario> result = null;
		Query consulta = db.query();
		consulta.constrain(SesionUsuario.class);
		consulta.descend("usr").constrain(idUsr);
		result = consulta.execute();
		if (result.size() > 0) {
			return (List<SesionUsuario>) result;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Alta de una nueva SesionUsuario sin repeticiones según los campos idUsr + fecha. 
	 * @param obj - la SesionUsuario a almacenar.
	 * @throws AccesoDatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) {
		SesionUsuario sesion = (SesionUsuario) obj;
		//actualiza datos
		db.store(sesion);
	}


	/**
	 * Elimina el objeto, dado el idUsr + fecha utilizado para el almacenamiento.
	 * @param idSesion - el idUsr + fecha de la SesionUsuario a eliminar.
	 * @return - la SesionUsuario eliminada.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public SesionUsuario baja(String idSesion) throws DatosException {
		SesionUsuario sesion = obtener(idSesion);
		if (sesion != null) {
			db.delete(sesion);
		}
		else {
			throw new DatosException("BAJA: La sesión no existe...");
		}
		return sesion;
	}

	/**
	 *  Actualiza datos de una SesionUsuario reemplazando el almacenado por el recibido.
	 *	@param obj - SesionUsuario con las modificaciones.
	 *  @throws DatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException {
		SesionUsuario sesion = (SesionUsuario) obj;
		String idSesion = sesion.getUsr().getIdUsr() + sesion.getFecha().toString();
		SesionUsuario sesionAux = (SesionUsuario) obtener(idSesion);
		if(sesionAux != null) {
			sesionAux.setUsr(sesion.getUsr());
			sesionAux.setFecha(sesion.getFecha());
			//sesionAux.setEstado(sesion.getEstado());
			db.store(sesionAux);
		}
		else {
			throw new DatosException("ACTUALIZAR: La SesionUsuario no existe...");
		}
	} 
	
	/**
	 * Obtiene el listado de todos las sesiones almacenadas.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		ObjectSet<SesionUsuario> result = null;
		Query consulta = db.query();
		consulta.constrain(SesionUsuario.class);	
		result = consulta.execute();	
		if (result.size() > 0) {
			for (SesionUsuario s: result)
				if (s != null)
					listado.append("\n" + s);
			return listado.toString();
		}
		else
			return null;
	}

}//class
