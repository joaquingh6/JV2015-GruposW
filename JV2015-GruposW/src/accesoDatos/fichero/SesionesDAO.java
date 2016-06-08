package accesoDatos.fichero;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO SesionUsuario utilizando un ArrayList persistente en fichero.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.1
 *  @source: SesionesDAO.java 
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

import accesoDatos.DatosException;
import accesoDatos.GestionDatos;
import accesoDatos.OperacionesDAO;
import config.Configuracion;
import modelo.Contraseña;
import modelo.Correo;
import modelo.Direccion;
import modelo.Mundo;
import modelo.Nif;
import modelo.Patron;
import modelo.Posicion;
import modelo.SesionUsuario;
import modelo.Usuario;
import modelo.SesionUsuario.EstadoSesion;
import modelo.Usuario.RolUsuario;
import util.Fecha;

public class SesionesDAO implements OperacionesDAO, Persistente {

	// Requerido por el Singleton.
	private static SesionesDAO instancia = null;

	// Elemento de almacenamiento. 
	private static ArrayList<SesionUsuario> sesionesUsuario;
	private static File fSesiones;

	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 * @throws DatosException 
	 */
	private SesionesDAO() throws DatosException {
		sesionesUsuario = new ArrayList<SesionUsuario>();
		fSesiones = new File(Configuracion.get().getProperty("sesiones.nombreFichero"));
		recuperarDatos();
	}

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static SesionesDAO getInstancia() {
		if (instancia == null) {
			try {
				instancia = new SesionesDAO();
			} catch (DatosException e) {
				// No hay datos.
				cargarPredeterminados();
			}
		}
		return instancia;
	}
	
	/**
	 *  Método para generar de datos predeterminados.
	 */
	private static void cargarPredeterminados() {
		// Sesion invitado "III0I" "Miau#0".
		// Obtiene usuario predeterminado.
		UsuariosDAO.getInstancia();
		Usuario usrDemo = UsuariosDAO.getInstancia().obtener("III0I");
		SesionUsuario sesionDemo = new SesionUsuario(usrDemo, new Fecha(), EstadoSesion.CERRADA);
		sesionesUsuario.add(sesionDemo);
		guardarDatos(sesionesUsuario);
	}

	//OPERACIONES DE PERSISTENCIA.
	/**
	 *  Recupera el Arraylist sesionesUsuario almacenados en fichero. 
	 * @throws DatosException 
	 */
	@Override
	public void recuperarDatos() throws DatosException {
		try {
			if (fSesiones.exists()) {
				FileInputStream fisSesiones = new FileInputStream(fSesiones);
				ObjectInputStream oisSesiones = new ObjectInputStream(fisSesiones);
				sesionesUsuario = (ArrayList<SesionUsuario>) oisSesiones.readObject();
				oisSesiones.close();
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
	 *  Guarda el Arraylist de sesiones de usuarios en fichero.
	 */
	@Override
	public void guardarDatos() {
		guardarDatos(sesionesUsuario);
	}
	
	/**
	 *  Guarda la lista recibida en el fichero de datos.
	 */
	private static void guardarDatos(List<SesionUsuario> listaSesiones) {
		try {
			FileOutputStream fosSesiones = new FileOutputStream(fSesiones);
			ObjectOutputStream oosSesiones = new ObjectOutputStream(fosSesiones);
			oosSesiones.writeObject(sesionesUsuario);		
			oosSesiones.flush();
			oosSesiones.close();
		} 
		catch (IOException e) {}	
	}

	//OPERACIONES DAO
	/**
	 * Búsqueda de sesión por idSesion.
	 * @param idSesion - el idUsr+fecha a buscar.
	 * @return - la sesión encontrada; null si no existe.
	 */
	@Override
	public SesionUsuario obtener(String idSesion)  {
		int inicio = 0;
		int fin = sesionesUsuario.size() - 1;
		int medio;
		int comparacion;					// auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;
			comparacion = sesionesUsuario.get(medio).getIdSesion().compareToIgnoreCase(idSesion);
			if (comparacion == 0) {
				return sesionesUsuario.get(medio);
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
	 * Búsqueda de Sesion dado un objeto, reenvía al método que utiliza idSesion.
	 * @param obj - la SesionUsuario a buscar.
	 * @return - la Sesion encontrada; null si no existe.
	 */
	@Override
	public SesionUsuario obtener(Object obj)  {
		return this.obtener(((SesionUsuario) obj).getIdSesion());
	}	

	/**
	 * Búsqueda de todas la sesiones de un mismo usuario.
	 * @param idUsr - el identificador de usuario a buscar.
	 * @return - Sublista con las sesiones encontrada; null si no existe ninguna.
	 */
	public List<SesionUsuario> obtenerTodasMismoUsr(String idUsr)  {
		int inicio = 0;
		int fin = sesionesUsuario.size() - 1;
		int medio;	
		int comparacion;					// auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;
			comparacion = sesionesUsuario.get(medio).getUsr().getIdUsr().compareToIgnoreCase(idUsr);
			if (comparacion == 0) {
				return separarSesionesUsr(medio);
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
	 * Separa en una lista independiente de todas las sesiones de un mismo usuario.
	 * @param medio - el indice de una sesion almacenada.
	 * @return - Sublista con las sesiones encontrada; null si no existe ninguna.
	 */
	private List<SesionUsuario> separarSesionesUsr(int medio) {
		int primera = medio;
		String idUsr = sesionesUsuario.get(medio).getUsr().getIdUsr();
		// Localiza primera sesion del usuario.
		for (int i = medio+1; i > sesionesUsuario.size()
				&& sesionesUsuario.get(i).getUsr().getIdUsr().equals(idUsr); i++) {
			primera = i;
		}
		// Localiza ultima sesion del usuario.
		int ultima = medio;
		for (int i = medio-1; i < 0
				&& sesionesUsuario.get(i).getUsr().getIdUsr().equals(idUsr); i--) {
			ultima = i;
		}
		// devuelve la sublista de sesiones buscadas.
		return sesionesUsuario.subList(primera, ultima+1);
	}
	
	/**
	 * Alta de una nueva SesionUsuario en orden y sin repeticiones según IdUsr + fecha. 
	 * Busca previamente la posición que le corresponde por búsqueda binaria.
	 * @param obj - la SesionUsuario a almacenar.
	 * @throws DatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		SesionUsuario sesion = (SesionUsuario) obj;
		int inicio = 0;
		int fin = sesionesUsuario.size() - 1;
		int medio = 0;
		int comparacion;					    // auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;			// Calcula posición central.
			// compara los dos id. Obtiene < 0 si id va después que medio.
			comparacion = sesionesUsuario.get(medio).getIdSesion().compareToIgnoreCase(sesion.getIdSesion());
			if (comparacion == 0) {			
				throw new DatosException("ALTA: La SesionUsuario ya existe...");   				  
			}		
			if (comparacion < 0) {
				inicio = medio + 1;
			}			
			else {
				fin = medio - 1;
			}
		}	
		sesionesUsuario.add(inicio, sesion); 	// Inserta la sesion en orden.
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param idSesion - identifcador de la SesionUsuario a eliminar.
	 * @return - el SesionUsuario eliminada.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public SesionUsuario baja(String idSesion) throws DatosException {
		SesionUsuario sesion = obtener(idSesion);
		if (sesion != null) {
			// Elimina la sesion del almacen de datos.
			sesionesUsuario.remove(sesion);
		}	
		else {
			throw new DatosException("BAJA: La SesionUsuario no existe...");
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
		SesionUsuario sesionAux = obtener(sesion);
		if (sesionAux != null) {	
			sesionAux.setUsr(sesion.getUsr());
			sesionAux.setFecha(sesion.getFecha());
			sesionAux.setEstado(sesion.getEstado());
			// Actualización
			sesionesUsuario.set(sesionesUsuario.indexOf(sesion), sesionAux);
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
		for (SesionUsuario sesiones: sesionesUsuario) {
			if (sesiones != null) {
				listado.append("\n" + sesiones);
			}
		}
		return listado.toString();
	}

}//class
