package accesoDatos.fichero;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Simulacion utilizando un ArrayList y un Hashtable
 *  persistentes en ficheros.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.1
 *  @source: SimulacionesDAO.java 
 *  @version: 1.1 - 2016/05/23 
 *  @author: ajp
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Simulacion utilizando un ArrayList persistente en fichero.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2
 *  @source: SimulacionesDAO.java 
 *  @version: 1.0 - 2016/05/23 
 *  @author: ajp
 */
import java.util.ArrayList;
import java.util.List;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import modelo.Simulacion;

public class SimulacionesDAO implements OperacionesDAO, Persistente {
	
	// Requerido por el Singleton. 
	private static SimulacionesDAO instancia;
	
	// Elemento de almacenamiento.
	private ArrayList<Simulacion> datosSimulaciones;
	private File fSimulaciones;
	
	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private SimulacionesDAO() {
		datosSimulaciones = new ArrayList<Simulacion>();
		fSimulaciones = new File("simulaciones.dat");
		try {
			recuperarDatos();
		} catch (DatosException e) {}
	}

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
	
	//OPERACIONES DE PERSISTENCIA.
	/**
	 *  Recupera el Arraylist datosSimulaciones almacenados en fichero. 
	 * @throws DatosException 
	 */
	@Override
	public void recuperarDatos() throws DatosException {
		try {
			if (fSimulaciones.exists()) {
				FileInputStream fisSimulaciones = new FileInputStream(fSimulaciones);
				ObjectInputStream oisSimulaciones = new ObjectInputStream(fisSimulaciones);
				datosSimulaciones = (ArrayList<Simulacion>) oisSimulaciones.readObject();
				oisSimulaciones.close();
			}
			else {
				throw new DatosException("El fichero de datos no existe...");
			}
		} 
		catch (ClassNotFoundException e) {}
		catch (IOException e) {}
	}
	
	/**
	 *  Guarda el Arraylist de simulaciones de usuarios en fichero.
	 */
	@Override
	public void guardarDatos() {
		try {
			FileOutputStream fosSimulaciones = new FileOutputStream(fSimulaciones);
			ObjectOutputStream oosSesiones = new ObjectOutputStream(fosSimulaciones);
			oosSesiones.writeObject(datosSimulaciones);		
			oosSesiones.flush();
			oosSesiones.close();
		} 
		catch (IOException e) {}
	}
	
	// OPERACIONES DAO
	/**
	 * Búsqueda binaria de Simulacion dado idUsr y fecha.
	 * @param idSimulacion - el idUsr+fecha de la Simulacion a buscar. 
	 * @return - la Simulacion encontrada; null si no existe.
	 */	
	public Simulacion obtener(String idSimulacion) {
		int inicio = 0;
		int fin = datosSimulaciones.size() - 1;
		int medio;
		int comparacion;					// auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;				
			comparacion = datosSimulaciones.get(medio).getIdSimulacion().compareToIgnoreCase(idSimulacion);
			if (comparacion == 0) {
				return datosSimulaciones.get(medio);
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
	 * Búsqueda de simulacion dado un objeto, reenvía al método que utiliza idSimulacion.
	 * @param obj - la Simulacion a buscar.
	 * @return - la Simulacion encontrada; null si no existe.
	 */
	public Simulacion obtener(Object obj)  {
		return this.obtener(((Simulacion) obj).getIdSimulacion());
	}
	
	/**
	 * Búsqueda de todas la simulaciones de un usuario.
	 * @param idUsr - el identificador de usuario a buscar.
	 * @return - Sublista con las simulaciones encontrada; null si no existe ninguna.
	 */
	public List<Simulacion> obtenerSimulacionesUsuario(String idUsr) {
		int inicio = 0;
		int fin = datosSimulaciones.size() - 1;
		int medio;
		int comparacion;					// auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;
			comparacion = datosSimulaciones.get(medio).getUsr().getIdUsr().compareToIgnoreCase(idUsr);
			if (comparacion == 0) {
				return generarLista(inicio);
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
	 * Separa en una lista independiente todas la simulaciones de un mismo usuario.
	 * @param inicio - el indice de una simulación ya encontrada.
	 * @return - Sublista con las simulaciones encontrada; null si no existe ninguna.
	 */
	private List<Simulacion> generarLista(int inicio) {
		int primera = inicio;
		String idUsr = datosSimulaciones.get(inicio).getUsr().getIdUsr();
		// Localiza primera simulación del mismo usuario.
		for (int i = inicio; i > datosSimulaciones.size() 
				&& datosSimulaciones.get(i).getUsr().getIdUsr().equals(idUsr); i++) {
			primera = i;
		}
		// Localiza ultima simulación del mismo usuario.
		int ultima = inicio;
		for (int i = inicio; i < 0 
				&& datosSimulaciones.get(i).getUsr().getIdUsr().equals(idUsr); i--) {
			ultima = i;
		}
		// devuelve la sublista de simulaciones buscadas.
		return datosSimulaciones.subList(primera, ultima+1);
	}

	/**
	 *  Alta de una nueva Simulacion en orden y sin repeticiones según los idUsr más fecha. 
	 *  Busca previamente la posición que le corresponde por búsqueda binaria.
	 *  @param obj - Simulación a almacenar.
	 *  @throws DatosException - si ya existe.
	 */	
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		Simulacion simulacion = (Simulacion) obj;
		int inicio = 0;
		int fin = datosSimulaciones.size() - 1;
		int medio;
		int comparacion;					// auxiliar para la comparación de String
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;				
			comparacion = datosSimulaciones.get(medio).getIdSimulacion().compareToIgnoreCase(simulacion.getIdSimulacion());
			if (comparacion == 0) {
				throw new DatosException("La Simulacion ya existe...");      
			}
			if (comparacion < 0) {			// id va después alfabéticamente 
				inicio = medio + 1;
			}
			else {				 			// id va antes alfabéticamente
				fin = medio - 1;
			}
		}
		datosSimulaciones.add(inicio, simulacion); 	// Inserta la simulación en orden.
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param idSimulacion - identifcador de la Simulacion a eliminar.
	 * @return - el Simulacion eliminada.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public Simulacion baja(String idSimulacion) throws DatosException {
		Simulacion simulacion = obtener(idSimulacion);
		if (simulacion != null) {
			// Elimina la simulacion del almacen de datos.
			datosSimulaciones.remove(simulacion);
		}	
		else {
			throw new DatosException("BAJA: La Simulacion no existe...");
		}
		return simulacion;
	}
	
	/**
	 *  Actualiza datos de una Simulacion reemplazando el almacenado por el recibido.
	 *	@param obj - Patron con las modificaciones.
	 *  @throws DatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException {
		Simulacion simulacion = (Simulacion) obj;
		Simulacion simulacionAux = obtener(simulacion);
		if (simulacionAux != null) {	
			simulacionAux.setUsr(simulacion.getUsr());
			simulacionAux.setFecha(simulacion.getFecha());
			//simulacionAux.(simulacion.getEstado());
			// Actualización
			datosSimulaciones.set(datosSimulaciones.indexOf(simulacion), simulacionAux);
		}	
		else {
			throw new DatosException("ACTUALIZAR: La Simulaciones no existe...");
		}
	}

	/**
	 * Obtiene el listado de todos las simulaciones almacenadas.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		for (Simulacion simulacion: datosSimulaciones) {
			if (simulacion != null) {
				listado.append("\n" + simulacion);
			}
		}
		return listado.toString();
	}
	
} //class
