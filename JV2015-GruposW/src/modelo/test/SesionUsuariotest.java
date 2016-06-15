package modelo.test;

	import java.io.Serializable;

import modelo.SesionUsuario;
import modelo.Usuario;
import util.Fecha;

	/** 
	 * Proyecto: Juego de la vida. 
	 *  Joaquin Garcia
	 *  prueba 3
	 */

	public class SesionUsuariotest implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// Atributos	
		private Usuario usr;   // Concreta la relación de composición del modelo UML
		private Fecha fecha; 
		
		// Constructores
		
		/**
		 * @param usr
		 * @param fecha
		 */
		public SesionUsuariotest(Usuario usr, Fecha fecha) {
			setUsr(usr);
			setFecha(fecha);
		}
		
		public SesionUsuariotest(){
			this(new Usuario(), new Fecha());
		}

		public SesionUsuariotest(SesionUsuario su){
			this(new Usuario(su.getUsr()), new Fecha(su.getFecha()));
		}
		
		
		// Métodos de acceso
		
		public Usuario getUsr() {
			return usr;
		}
		
		public Fecha getFecha() {
			return fecha;
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
		
		// Métodos redefinidos
		
		/**
		 * Redefine el método heredado de la clase Objecto.
		 * @return el texto formateado del estado (valores de atributos) 
		 * del objeto de la clase SesionUsuario  
		 */
		@Override
		public String toString() {
			return  "\n" + usr
					+ String.format("\n fecha: \t\t%s", fecha);
		}

	} // class