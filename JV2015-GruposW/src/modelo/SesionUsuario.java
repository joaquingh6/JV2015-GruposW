package modelo;

/** 
 * Proyecto: Juego de la vida.
 *  Implementa el concepto de SesionUsuario según el modelo1
 *  En esta versión no se han aplicado la mayoría de los estándares 
 *  de diseño OO dirigidos a conseguir un "código limpio". 
 *  @since: prototipo1.0
 *  @source: SesionUsuario.java 
 *  @version: 2.0 - 11/04/2016 
 *  @author: ajp
 */
import java.io.Serializable;

import util.Fecha;

public class SesionUsuario implements Serializable {
	
	public enum EstadoSesion { EN_PREPARACION, ACTIVA, CERRADA }

	// Atributos	
	private Usuario usr;  
	private Fecha fecha; 
	private EstadoSesion estado;
	
	/**
	 * @param usr
	 * @param fecha
	 */
	public SesionUsuario(Usuario usr, Fecha fecha, EstadoSesion estado) {
		setUsr(usr);
		setFecha(fecha);
		setEstado(estado);
	}
	
	public SesionUsuario(){
		this(new Usuario(), new Fecha(), EstadoSesion.EN_PREPARACION);
	}

	public SesionUsuario(SesionUsuario su){
		this(su.usr, new Fecha(su.fecha), su.estado);
	}
	
	
	// Métodos de acceso
	
	public Usuario getUsr() {
		return usr;
	}
	
	public Fecha getFecha() {
		return fecha;
	}

	public EstadoSesion getEstado() {
		return estado;
	}
	/**
	 * Obtiene idSesion concatenando idUsr + un número como texto con el formato:
	 * año+mes+dia+hora+minuto+segundo de la fecha de sesión.
	 * @return idSesion único generado.
	 */
	public String getIdSesion() {
		return usr.getIdUsr() + fecha.getAño() + fecha.getMes() + fecha.getDia() 
		+ fecha.getHora() + fecha.getMinuto() + fecha.getSegundo();
	}
	
	public void setUsr(Usuario usr) {
		assert usr != null;
		this.usr = usr;
	}
	
	public void setFecha(Fecha fecha) {
		assert fechaSesionValida(fecha);
		this.fecha = fecha;
	}
	
	/**
	 * Comprueba validez de una fecha.
	 * @param fecha.
	 * @return true si cumple.
	 */
	private boolean fechaSesionValida(Fecha fecha) {
		if (fecha != null
				&& fechaSesionCoherente(fecha)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba coherencia de una fecha de sesión.
	 * @param fecha.
	 * @return true si cumple.
	 */
	private boolean fechaSesionCoherente(Fecha fecha) {
		// Comprueba que fechaSesion no es, por ejemplo, del futuro
		// --Pendiente--
		return true;
	}
	
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(EstadoSesion estado) {
		this.estado = estado;
	}
	
	/**
	 * Redefine el método heredado de la clase Objecto.
	 * @return el texto formateado del estado (valores de atributos) 
	 * del objeto de la clase SesionUsuario  
	 */
	@Override
	public String toString() {
		return String.format("SesionUsuario \n[usr=%s, \nfecha=%s, \nestado=%s]",
				usr, fecha, estado);
	}

} // class
