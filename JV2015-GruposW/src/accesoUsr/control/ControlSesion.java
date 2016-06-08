package accesoUsr.control;

import accesoDatos.DatosException;
import accesoDatos.GestionDatos;
import accesoUsr.vista.VistaSesionTexto;
import config.Configuracion;
import modelo.Contraseña;
import modelo.SesionUsuario;
import modelo.SesionUsuario.EstadoSesion;
import modelo.Usuario;
import util.Fecha;

public class ControlSesion {
	private VistaSesionTexto vista;
	private Usuario usrSesion;
	private SesionUsuario sesion;
	private GestionDatos datos;

	public ControlSesion() {
		this(null);
	}

	public ControlSesion(String idUsr) {
		initControlSesion(idUsr);
	}

	private void initControlSesion(String idUsr) {
		datos = GestionDatos.getInstancia();
		vista = new VistaSesionTexto();
		iniciarSesionUsuario(idUsr); 
		new ControlSimulacion(datos.obtenerSimulacionesUsuario(usrSesion.getIdUsr()).get(0));
		datos.cerrar();
	}

	/**
	 * Controla el acceso de usuario 
	 * y registro de la sesión correspondiente.
	 * @param credencialUsr ya obtenida, puede ser null.
	 */
	private void iniciarSesionUsuario(String idUsr) {
		int intentos = new Integer(Configuracion.get().getProperty("sesion.intentosFallidos"));							// Contandor de intentos.
		String credencialUsr = idUsr;
		do {
			if (idUsr == null) {
				// Pide credencial usuario.
				vista.mostrar("Introduce el idUsr: ");
				credencialUsr = vista.pedirIdUsr();	
			}
			credencialUsr = credencialUsr.toUpperCase();
			// Pide contraseña.
			vista.mostrar("Introduce clave acceso: ");
			String clave = vista.pedirClaveAcceso();

			// Obtiene idUsr que corresponde.
			credencialUsr = datos.getEquivalenciaId(credencialUsr);	

			// Busca usuario coincidente con credencial.
			vista.mostrar(credencialUsr);
			usrSesion = datos.obtenerUsuario(credencialUsr);
			if ( usrSesion != null) {			
				if (usrSesion.getClaveAcceso().equals(new Contraseña(clave))) {
					registrarSesion();
					break;
				}
				intentos--;
				vista.mostrar("Credenciales incorrectas...");
				vista.mostrar("Quedan " + intentos + " intentos... ");
			}
		}
		while (intentos > 0);

		if (intentos <= 0){
			vista.mostrar("Fin de programa...");
			System.exit(0);	
		}
	}

	/**
	 * Crea la sesion de usuario 
	 */
	private void registrarSesion() {
		// Registra sesión.
		// Crea la sesión de usuario en el sistema.
		sesion = new SesionUsuario(usrSesion, new Fecha(), EstadoSesion.ACTIVA);
		try {
			datos.altaSesion(sesion);
		} catch (DatosException e) {
			e.printStackTrace();
		}	
		vista.mostrar("Sesión: " + sesion.getIdSesion()
		+ '\n' + "Iniciada por: " + usrSesion.getNombre() + " "
		+ usrSesion.getApellidos());	
	}

} //class
