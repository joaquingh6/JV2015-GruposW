
package accesoDatos.test;

/** 
 * Proyecto: Juego de la vida.
 *  Clase auxiliar para generar datos de prueba.
 *  @since: prototipo2.1
 *  @source: DatosPrueba.java 
 * @version: 1.0 - 9/05/2016 
 *  @author: ajp
 */

import java.util.ArrayList;
import java.util.Hashtable;

import accesoDatos.DatosException;
import accesoDatos.GestionDatos;
import modelo.*;
import modelo.SesionUsuario.EstadoSesion;
import modelo.Simulacion.EstadoSimulacion;
import modelo.Usuario.RolUsuario;
import util.Fecha;

public class DatosPrueba {

	// Fachada de datos
	private static GestionDatos datos = GestionDatos.getInstancia();
	
	/**
	 * Genera datos de prueba válidos dentro 
	 * del almacén de datos.
	 * @param numero - el número de usuarios a generar.
	 * @throws DatosException 
	 */
	public static void cargarUsuariosPrueba(int numero) {
		for (int i = 0; i < numero; i++) {
			Usuario usr =  new Usuario(new Nif("0234455"+i+"K"), "Pepe" + i,
					"López Pérez" +i, new Direccion("30012", "Alta", "10", "Murcia", "España"), 
					new Correo("pepe" + i + "@gmail.com"), new Fecha(1990, 11, 12), 
					new Fecha(2014, 12, 3), new Contraseña("Miau#" + i), RolUsuario.NORMAL);				
			try {
				datos.altaUsuario(usr);
			} catch (DatosException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Genera datos de prueba válidos dentro 
	 * de los almacenes de datos.
	 * @param numero - el número de sesiones a generar.
	 */
	public static void cargarSesionesPrueba(int numero) {
		for (int i = 0; i < numero; i++) {
			Usuario usr = datos.obtenerUsuario("PLP56" + (char) ('A' + i));
			if (usr != null) {
				SesionUsuario aux = new SesionUsuario();
				aux.setUsr(usr);
				aux.setFecha(new Fecha(2015, 1, 13));
				aux.setEstado(EstadoSesion.EN_PREPARACION);
				try {
					datos.altaSesion(aux);
				} catch (DatosException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Genera un objeto de prueba válido dentro del almacenes de datos.
	 */
	public static void cargarSimulacionPrueba() {
		cargarUsuariosPrueba(1);
		cargarMundoPrueba();
		Simulacion simulacionPrueba = new Simulacion(datos.obtenerUsuario("PLP0K"), 
				new Fecha(), datos.obtenerMundo("Prueba0"),	EstadoSimulacion.PREPARADA);
		try {
			datos.altaSimulacion(simulacionPrueba);
		} catch (DatosException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Genera datos de prueba válidos dentro 
	 * de los almacenes de datos.
	 */
	public static void cargarMundoPrueba() {	
		// En este array los 0 indican celdas con célula muerta y los 1 vivas
		byte[][] espacioPrueba =  new byte[][]{ 
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
		Mundo mundoPrueba = new Mundo("Prueba0", new ArrayList<Integer>(), 
									new Hashtable<Patron,Posicion>(), espacioPrueba);
		try {
			datos.altaMundo(mundoPrueba);
		} 
		catch (DatosException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genera datos de prueba válidos dentro 
	 * del almacén de datos.
	 * @param numero - el número de patrones a generar.
	 */
	public static void cargarPatronesPrueba(int numero) {
		for (int i = 0; i < numero; i++) {
			Patron patron = new Patron();
			patron.setNombre("Patron" + i);
			patron.setEsquema(new byte[0][0]);
			try {
				datos.altaPatron(patron);
			} catch (DatosException e) {
				e.printStackTrace();
			}
		}
	}

	public static void borrarDatosPrueba() {
		//
		
	}


} //class
