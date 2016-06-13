/** Proyecto: Juego de la vida.
 *  Implementa el concepto de Persona según el modelo2.  
 *  @since: prototipo2.0
 *  @source: Persona.java 
 *  @version: 1.0 - 14/03/2016 
 *  @author: ajp
 */
package modelo;

import java.io.Serializable;

import util.Fecha;
import util.Formato;

public class  Persona implements Serializable {

	// Atributos	
	protected Nif nif;
	protected String nombre;
	protected String apellidos;
	protected Direccion domicilio;
	protected Correo correo;
	protected Fecha fechaNacimiento;

	// Constructores
	/**
	 * Constructor convencional.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros que se corresponden con los atributos menos idUsr
	 * que se genera y controla internamente.
	 */
	public Persona(Nif nif, String nombre, String apellidos, 
			Direccion domicilio, Correo correo, Fecha fechaNacimiento) {
		setNif(nif);
		setNombre(nombre);
		setApellidos(apellidos);
		setDomicilio(domicilio);
		setCorreo(correo);
		setFechaNacimiento(fechaNacimiento);
	}

	/**
	 * Constructor por defecto.
	 * Establece el valor inicial, por defecto, de cada uno de los atributos.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Persona(){
		this(new Nif(), "Nombre", "Apellido Apellido", 
				new Direccion(), new Correo(), new Fecha());
	}

	/**
	 * Constructor copia.
	 * Establece el valor inicial de cada uno de los atributos a partir de
	 * los valores obtenidos de un objeto de su misma clase, recibido como parámetro.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Persona(Persona usr) {
		this(usr.nif, usr.nombre, usr.apellidos, 
				usr.domicilio, usr.correo, usr.fechaNacimiento);
	}

	// Métodos de acceso.

	public Nif getNif() {
		return nif;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public Direccion getDomicilio() {
		return domicilio;
	}

	public Fecha getFechaNacimiento() {
		return fechaNacimiento;
	}

	public Correo getCorreo() {
		return correo;
	}

	public void setNif(Nif nif) {
		assert nif != null;
		this.nif = nif;
	}

	public void setNombre(String nombre) {
		assert nombreValido(nombre);
		this.nombre = nombre;
	}

	/**
	 * Comprueba validez del un nombre.
	 * @param nombre.
	 * @return true si cumple.
	 */
	private boolean nombreValido(String nombre) {
		if (nombre != null
				&& util.Formato.validar(nombre, Formato.PATRON_NOMBRE_PERSONA)) {
			return true;
		}
		return false;
	}
	
	public void setApellidos(String apellidos) {
		assert apellidosValidos(apellidos);
		this.apellidos = apellidos;
	}

	/**
	 * Comprueba validez de los apellidos.
	 * @param apellidos.
	 * @return true si cumple.
	 */
	private boolean apellidosValidos(String apellidos) {
		if (apellidos != null
				&& util.Formato.validar(apellidos, Formato.PATRON_APELLIDOS)) {
			return true;
		}
		return false;
	}
	
	public void setDomicilio(Direccion domicilio) {
		assert domicilio != null;
		this.domicilio = domicilio;
	}

	
	public void setFechaNacimiento(Fecha fechaNacimiento) {
		assert fechaNacimientoValida(fechaNacimiento); 
		this.fechaNacimiento = fechaNacimiento;
	}
	
	/**
	 * Comprueba validez de una fecha de nacimiento.
	 * @param texto.
	 * @return true si cumple.
	 */
	private boolean fechaNacimientoValida(Fecha fechaNacimiento) {
		if (fechaNacimiento != null
				&&  fechaNacimientoCoherente(fechaNacimiento)) {
			return true;
		}
		return false;
	}

	/**
	 * Comprueba coherencia de una fecha de nacimiento.
	 * @param fechaNacimiento.
	 * @return true si cumple.
	 */
	private boolean fechaNacimientoCoherente(Fecha fechaNacimiento) {
		// Comprueba que fechaNacimiento no es, por ejemplo, del futuro
		// --Pendiente--
		return true;
	}
	
	public void setCorreo(Correo correo) {
		assert correo != null;
		this.correo = correo;
	}

	/**
	 * Redefine el método heredado de la clase Objecto.
	 * @return el texto formateado del estado (valores de atributos) 
	 * del objeto de la clase Usuario  
	 */
	@Override
	public String toString() {
	
		return String.format(
				"\n nif: \t\t%s,"
				+ "\n nombre: \t%s,"
				+ "\n apellidos: \t%s,"
				+ "\n domicilio: \t%s,"
				+ "\n correo: \t%s,"
				+ "\n fechaNacimiento: \t%s,",
				nif, nombre, apellidos, domicilio, correo, 
				fechaNacimiento);		
	}

} // class

