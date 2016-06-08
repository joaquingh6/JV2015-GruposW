package modelo;

import java.io.Serializable;

import config.Configuracion;
import util.Fecha;

/** Proyecto: Juego de la vida.
 *  Implementa el concepto de Usuario según el modelo2.
 *  Hereda de Persona.  
 *  @since: prototipo1.0
 *  @source: Usuario.java 
 *  @version: 2.0 - 14/03/2016 
 *  @author: ajp
 */

public class Usuario extends Persona implements Serializable {

	public enum RolUsuario {
		INVITADO, 
		NORMAL, 
		ADMINISTRADOR
	}

	// Atributos	
	private String idUsr;
	private Fecha fechaAlta;
	private Contraseña claveAcceso;
	private RolUsuario rol;

	// Constructores
	/**
	 * Constructor convencional.
	 * Establece el valor inicial de cada uno de los atributos.
	 * Recibe parámetros que se corresponden con los atributos menos idUsr
	 * que se genera y controla internamente.
	 */
	public Usuario(Nif nif, String nombre, String apellidos, 
			Direccion domicilio, Correo correo, Fecha fechaNacimiento,
			Fecha fechaAlta, Contraseña claveAcceso, RolUsuario rol) {
		
		super(nif, nombre, apellidos, domicilio, correo, fechaNacimiento);
		idUsr = generarIdUsr();
		setFechaAlta(fechaAlta);
		setClaveAcceso(claveAcceso);
		setRol(rol);
	}

	/**
	 * Constructor por defecto.
	 * Establece el valor inicial, por defecto, de cada uno de los atributos.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Usuario(){
		this(new Nif(), Configuracion.get().getProperty("usuario.invitado"), "Invitado Invitado", 
				new Direccion(), new Correo(), new Fecha(), 
				new Fecha(), new Contraseña(), RolUsuario.INVITADO);
	}

	/**
	 * Constructor copia.
	 * Establece el valor inicial de cada uno de los atributos a partir de
	 * los valores obtenidos de un objeto de su misma clase, recibido como parámetro.
	 * Llama al constructor convencional de la propia clase.
	 */
	public Usuario(Usuario usr) {
		this(usr.nif, usr.nombre, usr.apellidos, usr.domicilio, 
				usr.correo, usr.fechaNacimiento, 
				usr.fechaAlta, usr.claveAcceso, usr.rol);
	}

	// Métodos de acceso.

	public String getIdUsr() {
		return idUsr;
	}

	public Contraseña getClaveAcceso() {
		return claveAcceso;
	}

	public Fecha getFechaAlta() {
		return fechaAlta;
	}

	public RolUsuario getRol() {
		return rol;
	}

	@Override
	public void setNif(Nif nif) {
		assert nif != null;
		super.nif = nif;
		// Detecta que no es tiempo de constructor.
		if (this.idUsr != null) {
			reGenerarIdUsr();
		}
	}

	private void reGenerarIdUsr() {
		if (nif != null 
				&& nombre != null
				&& apellidos != null) {
			idUsr = generarIdUsr();
		}
	}

	/**
	 * Genera un identificador interno de usuario.
	 */
	private String generarIdUsr() {
		StringBuilder idUsr = new StringBuilder();
		// Primera letra nombre
		idUsr.append(nombre.charAt(0)); 

		// Primera letra de los apellidos
		String[] apellidos = this.apellidos.split(" ");
		idUsr.append(apellidos[0].charAt(0));
		if (apellidos.length > 1) {
			idUsr.append(apellidos[1].charAt(0));
		}

		// Últimos dos caracteres del nif
		idUsr.append(nif.getTexto().substring(nif.getTexto().length()-2));	

		// Comprueba que no existe y varía si es necesario.
		// ¿Cuantas veces debería intentarlo?
		while (buscarUsuario(idUsr.toString()) != null) {
			variarIdUsr(idUsr);
		}
		return idUsr.toString().toUpperCase();
	}

	private Usuario buscarUsuario(String idUsr) {
		// Busca y devuelve el usuario según su idUsr
		//--Pendiente--
		return null;
	}

	/**
	 * Varía un identificador interno de usuario.
	 * @param idUsr.
	 */
	private void variarIdUsr(StringBuilder idUsr) {
		if (idUsr.charAt(4) == 'Z') {
			idUsr.setCharAt(4, 'A');
		}
		else {		
			// Cambia la última letra con la siguiente en el alfabeto.
			idUsr.setCharAt(4, (char) (idUsr.charAt(4) + 1));
		}
	}

	public void setClaveAcceso(Contraseña claveAcceso) {
		assert claveAcceso != null;
		this.claveAcceso = claveAcceso;
	}

	public void setFechaAlta(Fecha fechaAlta) {
		assert fechaAltaValida(fechaAlta);
		this.fechaAlta = fechaAlta;
	}

	/**
	 * Comprueba validez de una fecha de alta.
	 * @param fechaAlta.
	 * @return true si cumple.
	 */
	private boolean fechaAltaValida(Fecha fechaAlta) {
		if (fechaAlta != null
				&& fechaAltaCoherente(fechaAlta)) {
			return true;
		}
		return false;
	}

	/**
	 * Comprueba coherencia de una fecha de alta.
	 * @param fechaAlta.
	 * @return true si cumple.
	 */
	private boolean fechaAltaCoherente(Fecha fechaAlta) {
		// Comprueba que fechaAlta no es, por ejemplo, del futuro
		// --Pendiente--
		return true;
	}

	public void setRol(RolUsuario rol) {
		assert rol != null;
		this.rol = rol;
	}

	// Métodos redefinidos

	/**
	 * Redefine el método heredado de la clase Objecto.
	 * @return el texto formateado del estado (valores de atributos) 
	 * del objeto de la clase Usuario  
	 */
	@Override
	public String toString() {

		return String.format(
				" nif: \t\t%s,"
						+ "\n nombre: \t%s,"
						+ "\n apellidos: \t%s,"
						+ "\n idUsr: \t%s,"
						+ "\n domicilio: \t%s,"
						+ "\n correo: \t%s,"
						+ "\n fechaNacimiento: \t%s,"
						+ "\n fechaAlta: \t%s,"
						+ "\n claveAcceso: \t%s,"
						+ "\n rol: \t\t%s", 
						nif, nombre, apellidos, idUsr, domicilio, correo, 
						fechaNacimiento, fechaAlta, claveAcceso, rol);		
	}

} // class

