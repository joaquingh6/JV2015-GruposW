package modelo;
/** 
 * Proyecto: Juego de la vida.
 *  Organiza aspectos de gestión de la simulación 
 *  según el modelo2.1
 *  @since: prototipo2.1
 *  @source: Simulacion.java 
 *  @version: 1.0 - 24/04/2016
 *  @author: ajp
 */

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import util.Fecha;

public class Simulacion implements Serializable {
	
	public enum EstadoSimulacion  {
		PREPARADA, INICIADA, COMPLETADA
	}
	
	//Atributos
	private Usuario usr;
	private Fecha fecha;
	private Mundo mundo;
	private EstadoSimulacion estado;

	/**
	 * Constructor convencional.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros que se corresponden con los atributos.
	 * Utiliza métodos set... para la posible verificación.
	 * @param usr
	 * @param fecha
	 * @param mundo
	 * @param estado
	 */
	public Simulacion(Usuario usr, Fecha fecha, Mundo mundo, EstadoSimulacion estado) {
		setUsr(usr);
		setFecha(fecha);
		setMundo(mundo);
		setEstado(estado);
	}

	/**
	 * Constructor por defecto.
	 * Establece el valor inicial, por defecto, de cada uno de los atributos.
	 * Llama al constructor convencional de la propia clase.
	 * @throws ModeloException 
	 */
	public Simulacion()  {
		this(new Usuario(), new Fecha(), new Mundo(), EstadoSimulacion.PREPARADA);
	}

	/**
	 * Constructor copia.
	 * Establece el valor inicial de cada uno de los atributos a partir de
	 * los valores obtenidos de un objeto de su misma clase.
	 * El objeto Usuario es compartido (agregación).
	 * Llama al constructor convencional.
	 * @param s - la Simulacion a clonar
	 */
	public Simulacion(Simulacion s) {
		this(s.usr, new Fecha(s.fecha), new Mundo(s.mundo), s.estado);
	}
	
	public Usuario getUsr() {
		return usr;
	}

	public Mundo getMundo() {
		return mundo;
	}

	public Fecha getFecha() {
		return fecha;
	}

	public EstadoSimulacion getEstado() {
		return estado;
	}

	/**
	 * Obtiene idSesion concatenando idUsr + un número como texto con el formato:
	 * año+mes+dia+hora+minuto de la fecha de sesión.
	 * @return true si cumple.
	 */
	public String getIdSimulacion() {
		return usr.getIdUsr() + fecha.getAño() + fecha.getMes() + fecha.getDia() 
		+ fecha.getHora() + fecha.getMinuto();
	}
	
	public void setUsr(Usuario usr) {
		this.usr = usr;
	}

	public void setMundo(Mundo mundo) {
		this.mundo = mundo;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public void setEstado(EstadoSimulacion estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return String.format(
				"Simulacion [usr=%s, fecha=%s, mundo=%s, estado=%s]", usr,
				fecha, mundo, estado);
	}
	
} //class
