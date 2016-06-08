package accesoDatos.db4o;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Usuario utilizando base de datos db4o.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.2
 *  @source: UsuariosDAO.java 
 *  @version: 1.1 - 2016/06/02 
 *  @author: ajp
 */
import java.util.Hashtable;
import java.util.Map;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import config.Configuracion;
import modelo.Contraseña;
import modelo.Correo;
import modelo.Direccion;
import modelo.Nif;
import modelo.Usuario;
import modelo.Usuario.RolUsuario;
import util.Fecha;

public class UsuariosDAO  implements OperacionesDAO {

	// Requerido por el Singleton 
	private static UsuariosDAO instancia = null;

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
	public static UsuariosDAO getInstancia() {
		if (instancia == null) {
			instancia = new UsuariosDAO();
		}
		return instancia;
	}

	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private UsuariosDAO() {
		db = Conexion.getDB();
		db.store(new Hashtable<String,String>());
		if (obtener("III0I") == null) {
			cargarPredeterminados();
		}
	}

	/**
	 *  Método para generar de datos predeterminados.
	 */
	private void cargarPredeterminados() {
		String nombreUsr = Configuracion.get().getProperty("usuario.admin");
		String password = Configuracion.get().getProperty("usuario.passwordPredeterminada");	
		Usuario usrPredeterminado = new Usuario(new Nif("76543210A"), nombreUsr, "Admin Admin", 
				new Direccion("30012", "Iglesia", "0", "Murcia", "España"), 
				new Correo("jv.admin" + "@gmail.com"), new Fecha(), 
				new Fecha(), new Contraseña(password), RolUsuario.ADMINISTRADOR);
		db.store(usrPredeterminado);
		registrarEquivalenciaId(usrPredeterminado);
		nombreUsr = Configuracion.get().getProperty("usuario.invitado");
		usrPredeterminado = new Usuario(new Nif("06543210I"), nombreUsr, "Invitado Invitado", 
				new Direccion("30012", "Iglesia", "0", "Murcia", "España"), 
				new Correo("jv.invitado" + "@gmail.com"), new Fecha(), 
				new Fecha(), new Contraseña(password), RolUsuario.INVITADO);
		try {
			alta(usrPredeterminado);
		} catch (DatosException e) {}
		registrarEquivalenciaId(usrPredeterminado);
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
	 * Obtiene un Usuario dado su idUsr.
	 * @param id - el idUsr de Usuario a buscar.
	 * @return - el Usuario encontrado; null si no existe.
	 */	
	@Override
	public Usuario obtener(String idUsr) {
		ObjectSet<Usuario> result;
		Query consulta = db.query();
		consulta.constrain(Usuario.class);
		consulta.descend("idUsr").equals(idUsr);
		result = consulta.execute();
		if (result.size() > 0) {
			return result.get(0);
		}
		else {	
			return null;
		}
	}

	/**
	 * Búsqueda de Usuario dado un objeto, reenvía al método que utiliza idUsr.
	 * @param obj - el Usuario a buscar.
	 * @return - la Simulacion encontrada; null si no existe.
	 */
	@Override
	public Usuario obtener(Object obj)  {
		return this.obtener(((Usuario) obj).getIdUsr());
	}

	/**
	 *  Alta de un nuevo usuario en orden y sin repeticiones según el campo idUsr. 
	 *  Localiza previamente la posición que le corresponde por búsqueda binaria.
	 *	@param obj - Objeto a almacenar.
	 *  @throws AccesoDatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		Usuario usr = (Usuario) obj;
		if (obtener(usr.getIdUsr()) == null) {
			//actualiza datos
			db.store(usr);
			registrarEquivalenciaId(usr);
		}
		else {
			throw new DatosException("ALTA: El Usuario " + usr.getIdUsr() + " ya existe...");
		}
	} 

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param id - el identificador del objeto a eliminar.
	 * @return - el Objeto eliminado.
	 * @throws AccesoDatosException - si no existe.
	 */
	@Override
	public Usuario baja(String id) throws DatosException {
		Usuario usr = (Usuario) obtener(id);
		if (usr != null) {
			borrarEquivalenciaId(usr);
			db.delete(usr);
		}
		else {
			throw new DatosException("BAJA: El Usuario " + usr.getIdUsr() + " no existe...");
		}
		return usr;	
	} 

	/**
	 *  Actualiza datos de un Usuario reemplazando el almacenado por el recibido. 
	 *  Localiza la posición del almacenado por búsqueda binaria.
	 *	@param obj - Usuario con los cambios.
	 * @return 
	 *  @throws AccesoDatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException {
		Usuario usr = (Usuario) obj;
		Usuario usrAux = (Usuario) obtener(usr.getIdUsr());
		if(usrAux != null) {
			cambiarEquivalenciaId(usrAux, usr);
			usrAux.setNif(usr.getNif());
			usrAux.setNombre(usr.getNombre());
			usrAux.setApellidos(usr.getApellidos());
			usrAux.setDomicilio(usr.getDomicilio());
			usrAux.setCorreo(usr.getCorreo());
			usrAux.setFechaNacimiento(usr.getFechaNacimiento());
			usrAux.setFechaAlta(usr.getFechaAlta());
			usrAux.setRol(usr.getRol());
			db.store(usrAux);
		}
		else {
			throw new DatosException("ACTUALIZAR: El Usuario " + usr.getIdUsr() + " no existe...");
		}
	} 

	/**
	 * Obtiene el listado de todos los usuarios almacenados.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder sb = new StringBuilder();
		ObjectSet<Usuario> result;
		Query consulta = db.query();
		consulta.constrain(Usuario.class);
		result = consulta.execute();
		for (Usuario u: result)
			if (u != null)
				sb.append("\n" + u);
		return sb.toString();
	}

	//GESTION equivalencias id
	/**
	 * Obtiene el idUsr usado internamente a partir de otro equivalente.
	 * @param id - la clave alternativa. 
	 * @return - El idUsr equivalente.
	 */
	public String obtenerEquivalencia(String id) {
		return obtenerMapaEquivalencias().get(id);
	}

	/**
	 * Obtiene el mapa de equivalencias de id para idUsr.
	 * @return el Hashtable almacenado.
	 */
	private Map<String,String> obtenerMapaEquivalencias() {
		//Obtiene mapa de equivalencias de id de acceso
		Query consulta = db.query();
		consulta.constrain(Hashtable.class);
		ObjectSet <Hashtable<String,String>> result = consulta.execute();
		return result.get(0);	
	}

	/**
	 * Registra las equivalencias de nif y correo para un idUsr.
	 * @param usuario
	 */
	private void registrarEquivalenciaId(Usuario usuario) {
		//Obtiene mapa de equivalencias
		Map<String,String> mapaEquivalencias = obtenerMapaEquivalencias();
		//Registra equivalencias 
		mapaEquivalencias.put(usuario.getIdUsr().toUpperCase(), usuario.getIdUsr().toUpperCase());
		mapaEquivalencias.put(usuario.getNif().getTexto().toUpperCase(), usuario.getIdUsr().toUpperCase());
		mapaEquivalencias.put(usuario.getCorreo().getTexto().toUpperCase(), usuario.getIdUsr().toUpperCase());
		//actualiza datos
		db.store(mapaEquivalencias);	
	}

	/**
	 * Elimina las equivalencias de nif y correo para un idUsr.
	 * @param usuario - el usuario para eliminar sus equivalencias de idUsr.
	 */
	private void borrarEquivalenciaId(Usuario usuario) {
		//Obtiene mapa de equivalencias
		Map<String,String> mapaEquivalencias = obtenerMapaEquivalencias();
		//Borra equivalencias 
		mapaEquivalencias.remove(usuario.getIdUsr());
		mapaEquivalencias.remove(usuario.getNif().getTexto());
		mapaEquivalencias.remove(usuario.getCorreo().getTexto());
		//actualiza datos
		db.store(mapaEquivalencias);	
	}

	/**
	 * Actualiza las equivalencias de nif y correo para un idUsr
	 * @param usrAntiguo - usuario con id's antiguos
	 * @param usrNuevo - usuario con id's nuevos
	 */
	private void cambiarEquivalenciaId(Usuario usrAntiguo, Usuario usrNuevo) {
		//Obtiene mapa de equivalencias
		Map<String,String> mapaEquivalencias = obtenerMapaEquivalencias();
		//Cambia equivalencias 
		mapaEquivalencias.replace(usrAntiguo.getIdUsr(), usrNuevo.getIdUsr().toUpperCase());
		mapaEquivalencias.replace(usrAntiguo.getNif().getTexto(), usrNuevo.getIdUsr().toUpperCase());
		mapaEquivalencias.replace(usrAntiguo.getCorreo().getTexto(), usrNuevo.getIdUsr().toUpperCase());
		//actualiza datos
		db.store(mapaEquivalencias);
	}

} //class