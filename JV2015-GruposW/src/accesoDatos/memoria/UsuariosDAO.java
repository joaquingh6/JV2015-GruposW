package accesoDatos.memoria;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Usuario utilizando un ArrayList y un Hashtable.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.1
 *  @source: UsuariosDAO.java 
 *  @version: 1.2 - 2016/06/05 
 *  @author: ajp
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

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

	// Requerido por el Singleton. 
	private static UsuariosDAO instancia = null;

	// Elementos de almacenamiento.
	private static ArrayList<Usuario> datosUsuarios;
	private static Map<String,String> equivalenciasId;

	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private UsuariosDAO()  {
		datosUsuarios = new ArrayList<Usuario>();
		equivalenciasId = new Hashtable<String, String>();
		cargarPredeterminados();
	}

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
	 *  Método para generar de datos predeterminados.
	 */
	private static void cargarPredeterminados() {
		String nombreUsr = Configuracion.get().getProperty("usuario.admin");
		String password = Configuracion.get().getProperty("usuario.passwordPredeterminada");	
		Usuario usrPredeterminado = new Usuario(new Nif("76543210A"), nombreUsr, "Admin Admin", 
					new Direccion("30012", "Iglesia", "0", "Murcia", "España"), 
					new Correo("jv.admin" + "@gmail.com"), new Fecha(), 
					new Fecha(), new Contraseña(password), RolUsuario.ADMINISTRADOR);
		datosUsuarios.add(usrPredeterminado);
		registrarEquivalenciaId(usrPredeterminado);
		nombreUsr = Configuracion.get().getProperty("usuario.invitado");
		password = Configuracion.get().getProperty("usuario.passwordPredeterminada");	
		usrPredeterminado = new Usuario(new Nif("06543210I"), nombreUsr, "Invitado Invitado", 
					new Direccion("30012", "Iglesia", "0", "Murcia", "España"), 
					new Correo("jv.invitado" + "@gmail.com"), new Fecha(), 
					new Fecha(), new Contraseña(password), RolUsuario.INVITADO);
		datosUsuarios.add(usrPredeterminado);
		registrarEquivalenciaId(usrPredeterminado);
	}

	/**
	 *  Cierra datos.
	 */
	@Override
	public void cerrar() {
		// Nada que hacer si no hay persistencia.
	}
	
	//OPERACIONES DAO
	/**
	 * Obtiene por búsqueda binaria un Usuario dado su id.
	 * @param idUsr - el idUsr de Usuario a buscar.
	 * @return - el Usuario encontrado; null si no existe.
	 */	
	@Override
	public Usuario obtener(String idUsr) {
		int comparacion;
		int inicio = 0;
		int fin = datosUsuarios.size() - 1;
		int medio;
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;
			comparacion = datosUsuarios.get(medio).getIdUsr().compareToIgnoreCase(idUsr);
			if (comparacion == 0) {
				return datosUsuarios.get(medio);
			}
			if (comparacion < 0){
				inicio = medio + 1;
			}
			else {
				fin = medio - 1;
			}
		}
		return null;
	}

	/**
	 * Búsqueda de Usuario dado un objeto, reenvía al método que utiliza idUsr.
	 * @param obj - el Usuario a buscar.
	 * @return - el Usuario encontrado; null si no existe.
	 */
	@Override
	public Usuario obtener(Object obj)  {
		return this.obtener(((Usuario) obj).getIdUsr());
	}	
	
	/**
	 * @param id - la clave alternativa. 
	 * @return - El idUsr equivalente.
	 */
	public String obtenerEquivalencia(String id) {
		return equivalenciasId.get(id);
	}
	
	/**
	 *  Alta de un nuevo usuario en orden y sin repeticiones según el campo idUsr. 
	 *  Localiza previamente la posición que le corresponde por búsqueda binaria.
	 *	@param usr - Objeto a almacenar.
	 *  @throws DatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		Usuario usr = (Usuario) obj;
		int comparacion;
		int inicio = 0;
		int fin = datosUsuarios.size() - 1;
		int medio = 0;
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;			// Calcula posición central.
			// compara los dos id. Obtiene < 0 si id va después que medio.
			comparacion = datosUsuarios.get(medio).getIdUsr()
					.compareToIgnoreCase(usr.getIdUsr());
			if (comparacion == 0) {			
				throw new DatosException("ALTA: El Usuario ya existe...");   				  
			}		
			if (comparacion < 0) {
				inicio = medio + 1;
			}			
			else {
				fin = medio - 1;
			}
		}	
		datosUsuarios.add(inicio, usr); 	// Inserta el usuario en orden.
		registrarEquivalenciaId(usr);		
	}

	/**
	 * Añade una nueva equivalencias para idUsr.
	 * @param usr
	 */
	private static void registrarEquivalenciaId(Usuario usr) {	 
		assert usr != null;
		equivalenciasId.put(usr.getIdUsr(), usr.getIdUsr());
		equivalenciasId.put(usr.getNif().getTexto(), usr.getIdUsr());
		equivalenciasId.put(usr.getCorreo().getTexto(), usr.getIdUsr());	
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param idUsr - el identificador del objeto a eliminar.
	 * @return - el Objeto eliminado.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public Object baja(String idUsr) throws DatosException {
		int inicio = 0;
		int fin = datosUsuarios.size() - 1;
		int medio;
		Usuario aux;
		int comparacion;					// auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;
			comparacion = datosUsuarios.get(medio).getIdUsr().compareToIgnoreCase(idUsr);
			if (comparacion == 0) {
				// Elimina
				aux = datosUsuarios.get(medio);
				datosUsuarios.remove(medio);
				equivalenciasId.remove(aux.getIdUsr(), aux.getIdUsr());
				return aux;
			}
			if (comparacion < 0) { 
				inicio = medio + 1;
			}
			else {
				fin = medio - 1; 
			}
		}
		throw new DatosException("BAJA: El Usuario no existe...");
	} 

	/**
	 *  Actualiza datos de un Usuario reemplazando el almacenado por el recibido. 
	 *  Localiza la posición del almacenado por búsqueda binaria.
	 *	@param obj - Usuario con los cambios.
	 *  @throws DatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException {
		assert obj != null;
		Usuario usr = (Usuario) obj;			// para conversión cast
		int inicio = 0;
		int fin = datosUsuarios.size()-1;
		int medio = 0;
		int comparacion;					// auxiliar para la comparación de String
		boolean noExisteUsuario = true;
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;     // calcula posición central
			// compara los dos id. Obtiene < 0 si id va después que medio
			comparacion = datosUsuarios.get(medio).getIdUsr().compareTo(usr.getIdUsr());
			if (comparacion == 0) {			// id coincide con el comparado
				reemplazar(usr, medio);
				noExisteUsuario = false;
				break;       				// ya actualizado  
			}
			if (comparacion < 0) { 			// id va después alfabéticamente 
				inicio = medio + 1;
			}
			else {				 			// id va antes alfabéticamente
				fin = medio - 1;
			}
		}
		if (noExisteUsuario) {
			throw new DatosException("ACTUALIZAR: No existe el Usuario...");
		}
	} 

	/**
	 *  Reemplaza un Usuario almacenado por el recibido. 
	 *	@param usr - Usuario con los cambios.
	 *  @param posicion - indice del elemento a reemplazar.
	 */
	private void reemplazar(Usuario usr, int posicion) {
		// Reemplaza elemento
		datosUsuarios.set(posicion, usr);  	
		// Reemplaza equivalencias de id de acceso
		equivalenciasId.replace(usr.getIdUsr(), usr.getIdUsr());
		equivalenciasId.replace(usr.getNif().getTexto(), usr.getIdUsr());
		equivalenciasId.replace(usr.getCorreo().getTexto(), usr.getIdUsr());	
	}

	/**
	 * Obtiene el listado de todos los usuarios almacenados.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		for (Usuario usuario: datosUsuarios) {
			if (usuario != null) {
				listado.append("\n" + usuario); 
			}
		}
		return listado.toString();
	}

} //class