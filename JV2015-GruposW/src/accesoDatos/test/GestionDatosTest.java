package accesoDatos.test;
/** 
 * Proyecto: Juego de la vida.
 *  Clase JUnit 4 para pruebas de GestionDatos.
 *  @since: prototipo2.1
 *  @source: GestionDatosTest.java 
 * @version: 1.0 - 9/05/2016 
 *  @author: ajp
 */
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import accesoDatos.DatosException;
import accesoDatos.GestionDatos;
import modelo.Contraseña;
import modelo.Correo;
import modelo.Direccion;
import modelo.Nif;
import modelo.SesionUsuario;
import modelo.SesionUsuario.EstadoSesion;
import modelo.Usuario;
import modelo.Usuario.RolUsuario;
import util.Fecha;

public class GestionDatosTest {

	GestionDatos datos;

	@Before
	public void datosPrueba() {
		datos = GestionDatos.getInstancia();
		// Carga datos de prueba
		DatosPrueba.cargarUsuariosPrueba(2);
		DatosPrueba.cargarMundoPrueba();
		DatosPrueba.cargarSimulacionPrueba();
		DatosPrueba.cargarSesionesPrueba(1);
		DatosPrueba.cargarPatronesPrueba(1);
	}

	@After
	public void borraDatosPrueba() {
		// Borra datos de prueba.
		DatosPrueba.borrarDatosPrueba();
	}
	
	@Test
	public void testGetInstancia() {
		assertSame(datos, GestionDatos.getInstancia());
	}

	@Test
	public void testCerrar() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerUsuarioId() {
		assertEquals(datos.obtenerUsuario("PLP5K").getIdUsr(), "PLP5K");
	}

	@Test
	public void testObtenerUsuarioObjeto() {
		// Usuario con idUsr "PLP5L"
		Usuario usr =  new Usuario(new Nif("12345675L"), "Pepe",
				"López Pérez", new Direccion("30012", "Alta", "10", "Murcia", "España"), 
				new Correo("pepe" + "@gmail.com"), new Fecha(1990, 11, 12), 
				new Fecha(2014, 12, 3), new Contraseña("Miau#32"), RolUsuario.NORMAL);
		try {
			// Usuario nuevo, que no existe.
			datos.altaUsuario(usr);
		} 
		catch (DatosException e) {
			e.printStackTrace();
		}
		// Busca el mismo Usuario almacenado.
		assertSame(usr, datos.obtenerUsuario(usr.getIdUsr()));
	}

	@Test
	public void testAltaUsuario() {
		fail("Not yet implemented");
	}

	@Test
	public void testBajaUsuario() {
		fail("Not yet implemented");
	}

	@Test
	public void testActualizarUsuario() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDatosUsuarios() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquivalenciaId() {
		// Usuario con idUsr "PLP5L"
				Usuario usr =  new Usuario(new Nif("12345675L"), "Pepe",
						"López Pérez", new Direccion("30012", "Alta", "10", "Murcia", "España"), 
						new Correo("pepe" + "@gmail.com"), new Fecha(1990, 11, 12), 
						new Fecha(2014, 12, 3), new Contraseña("Miau#32"), RolUsuario.NORMAL);
				try {
					// Usuario nuevo, que no existe.
					datos.altaUsuario(usr);
				} 
				catch (DatosException e) {
					e.printStackTrace();
				}
				assertEquals(datos.obtenerUsuario("PLP5L").getIdUsr(), "PLP5L");
				assertEquals(datos.obtenerUsuario("12345675L").getIdUsr(), "PLP5L");
				assertEquals(datos.obtenerUsuario("pepe@gmail.com").getIdUsr(), "PLP5L");
	}

	@Test
	public void testObtenerSesionString() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerSesionSesionUsuario() {
		fail("Not yet implemented");
	}

	@Test
	public void testAltaSesion() {
		// Usuario con idUsr "PLP5L"
				Usuario usr =  new Usuario(new Nif("12345675L"), "Pepe",
						"López Pérez", new Direccion("30012", "Alta", "10", "Murcia", "España"), 
						new Correo("pepe" + "@gmail.com"), new Fecha(1990, 11, 12), 
						new Fecha(2014, 12, 3), new Contraseña("Miau#32"), RolUsuario.NORMAL);

				SesionUsuario sesion = new SesionUsuario(usr, new Fecha(), EstadoSesion.EN_PREPARACION);
				try {
					datos.altaSesion(sesion);
				} catch (DatosException e) {
					e.printStackTrace();
				}
				assertSame(sesion, datos.obtenerSesion(sesion.getIdSesion()));
	}

	@Test
	public void testBajaSesionUsuario() {
		fail("Not yet implemented");
	}

	@Test
	public void testActualizarSesion() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDatosSesiones() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerSimulacionString() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerSimulacionSimulacion() {
		fail("Not yet implemented");
	}

	@Test
	public void testAltaSimulacion() {
		fail("Not yet implemented");
	}

	@Test
	public void testBajaSimulacion() {
		fail("Not yet implemented");
	}

	@Test
	public void testActualizarSimulacion() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDatosSimulaciones() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerMundoString() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerMundoMundo() {
		fail("Not yet implemented");
	}

	@Test
	public void testAltaMundo() {
		fail("Not yet implemented");
	}

	@Test
	public void testBajaMundo() {
		fail("Not yet implemented");
	}

	@Test
	public void testActualizarMundo() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDatosMundos() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerPatronString() {
		fail("Not yet implemented");
	}

	@Test
	public void testObtenerPatronPatron() {
		fail("Not yet implemented");
	}

	@Test
	public void testAltaPatron() {
		fail("Not yet implemented");
	}

	@Test
	public void testBajaPatron() {
		fail("Not yet implemented");
	}

	@Test
	public void testActualizarPatron() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDatosPatrones() {
		fail("Not yet implemented");
	}

}
