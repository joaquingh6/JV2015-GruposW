package accesoUsr.control;

import accesoDatos.GestionDatos;
import accesoUsr.vista.VistaSimulacionTexto;
import modelo.Mundo;
import modelo.SesionUsuario;
import modelo.Simulacion;

public class ControlSimulacion {
	GestionDatos datos = GestionDatos.getInstancia();
	final int CICLOS = 120;
	VistaSimulacionTexto vista;
	Simulacion simulacion;
	Mundo mundo;
	
	public ControlSimulacion(Simulacion simulacion) {
		this.simulacion = simulacion;
		initControlSimulacion();
	}
	
	private void initControlSimulacion() {	
		mundo = simulacion.getMundo();	
		vista = new VistaSimulacionTexto();
		arrancarSimulacion();	
	}

	/**
	 * Ejecuta una simulación del juego de la vida, en la consola,
	 * durante un número de CICLOS.
	 */
	public void arrancarSimulacion() {
		int gen = 0; 		//Generaciones
		do {
			vista.mostrar("\nGeneración: " + gen);
			vista.mostrarMundo(this);
			mundo = (Mundo) mundo.actualizarEstado();
			gen++;
		}
		while (gen <= CICLOS);
	}
	
	public Simulacion getSimulacion() {
		return simulacion;
	}
	
	public Mundo getMundo() {
		return mundo;
	}
	
} // class
