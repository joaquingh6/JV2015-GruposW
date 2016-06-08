package config;
/** 
 * Proyecto: Juego de la vida.
 *  Organiza y gestiona la configuración de la aplicación.
 *  Utiliza Properties para organizar y almacenar la configuración.
 *  @since: prototipo2.2
 *  @source: Configuracion.java 
 *  @version: 1.0 - 2016/05/5 
 *  @author: ajp
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {

	// Singleton
	private static Properties configuracion;

	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private Configuracion() {
		configuracion = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream("jv.config");
			configuracion.load(is);
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static Properties get() {
		if (configuracion == null) {
			new Configuracion();
		}
		return configuracion;
	}

} // class