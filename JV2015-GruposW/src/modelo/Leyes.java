package modelo;

/** Proyecto: Juego de la vida.
 *  Identifica  las leyes que rigen el Mundo según el modelo2.  
 *  @since: prototipo2.0
 *  @source: Leyes.java 
 *  @version: 1.0 - 14/03/2016 
 *  @author: ajp
 */

public interface Leyes {

	/**
	 * Establece la manera en que actualiza el estado de un objeto sometido a cambios 
	 * en una simulación. 
	 * responde a la regla: El tiempo transcurre y se producen cambios...
	 * @return 
	 */
	public Object actualizarEstado();
}
