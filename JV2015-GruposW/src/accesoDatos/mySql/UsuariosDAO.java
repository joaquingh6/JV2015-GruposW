/** 
 * Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos del almacenamiento del
 *  DTO Usuario utilizando base de datos mySQL.
 *  Colabora en el patron Fachada.
 *  @since: prototipo2.2
 *  @source: UsuariosDAO.java 
 *  @version: 1.1 - 2016/06/02 
 *  @author: ajp
 */

package accesoDatos.mySql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import config.Configuracion;
import modelo.Contraseña;
import modelo.Correo;
import modelo.Direccion;
import modelo.Nif;
import modelo.Usuario;
import modelo.Usuario.RolUsuario;
import util.Fecha;

public class UsuariosDAO implements OperacionesDAO {

	// Requerido por el patrón 
	private static UsuariosDAO instancia = null;

	private Connection db;
	private Statement sentenciaUsr;
	private Statement sentenciaId;
	private ResultSet rsUsuarios;
	private DefaultTableModel tmUsuarios; 	// Tabla presentable del resultado de la consulta.
	private List<Usuario> bufferObjetos; 	

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static UsuariosDAO getInstancia() {
		if (instancia == null) {
			instancia = new UsuariosDAO();
		}
		return instancia;
	}

	/**
	 * Constructor por defecto de uso interno.
	 */
	private UsuariosDAO() {
		inicializar();
		//System.out.println(obtener("III0I"));
		if (obtener("III0I") == null) {
			cargarPredeterminados();
		}
	}

	private void inicializar() {
		bufferObjetos = new ArrayList<Usuario>();
		db = Conexion.getDb();
		try {
			// Se crea dos Statement en la conexión a la BD, 
			//para realizar las consultas.

			sentenciaUsr = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			sentenciaId = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			sentenciaUsr.executeQuery("SELECT * FROM usuarios");
			sentenciaId.executeQuery("SELECT * FROM equivalid");
		}
		catch (SQLException e){
			crearTablaUsuarios();
			crearTablaEquivalId();
		}

		// Crea el tableModel y el buffer de objetos para usuarios.
		tmUsuarios = new DefaultTableModel();
		bufferObjetos = new ArrayList<Usuario>(); 
	}

	private void crearTablaUsuarios() {
		try {
			// Se crea un Statement en la conexión a la BD, para realizar la operación.
			Statement s = db.createStatement();

			// Crea la tabla usuarios
			s.executeUpdate("CREATE TABLE usuarios (" 
					+ "idUsr VARCHAR(5) NOT NULL,"
					+ "nif VARCHAR(9) NOT NULL," 
					+ "nombre VARCHAR(45) NOT NULL," 
					+ "apellidos VARCHAR(45) NOT NULL," 
					+ "domicilio VARCHAR(45) NOT NULL," 
					+ "correo VARCHAR(45) NOT NULL," 
					+ "fechaNacimiento DATE," 
					+ "fechaAlta DATE," 
					+ "claveAcceso VARCHAR(45) NOT NULL," 	
					+ "rol VARCHAR(20) NOT NULL," 
					+ "PRIMARY KEY(idUsr));");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void crearTablaEquivalId() {
		try {
			// Se crea un Statement en la conexión a la BD, para realizar la operación
			Statement s = db.createStatement();

			// Crea la tabla equivalencias
			s.executeUpdate("CREATE TABLE equivalid (" 
					+ "equival VARCHAR(45) NOT NULL,"
					+ "idUsr VARCHAR(5) NOT NULL,"  
					+ "PRIMARY KEY(equival));");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Método para generar de datos predeterminados.
	 */
	private void cargarPredeterminados() {
		String nombreUsr = Configuracion.get().getProperty("usuario.admin");
		String password = Configuracion.get().getProperty("usuario.passwordPredeterminada");	
		Usuario usrPredeterminado = new Usuario(new Nif("76543210A"), nombreUsr, "Admin Admin", 
				new Direccion("30012", "Iglesia", "0", "Murcia", "España"), 
				new Correo("jv.admin" + "@gmail.com"), new Fecha(), 
				new Fecha(), new Contraseña(password), RolUsuario.ADMINISTRADOR);
		try {
			alta(usrPredeterminado);
		} 
		catch (DatosException e1) {}
		registrarEquivalenciaId(usrPredeterminado);

		nombreUsr = Configuracion.get().getProperty("usuario.invitado");
		usrPredeterminado = new Usuario(new Nif("06543210I"), nombreUsr, "Invitado Invitado", 
				new Direccion("30012", "Iglesia", "0", "Murcia", "España"), 
				new Correo("jv.invitado" + "@gmail.com"), new Fecha(), 
				new Fecha(), new Contraseña(password), RolUsuario.INVITADO);
		try {
			alta(usrPredeterminado);
		} 
		catch (DatosException e) {}
		registrarEquivalenciaId(usrPredeterminado);
	}

	// MÉTODOS PARA LOS TABLEMODEL'S 
	/**
	 * Crea las columnas del TableModel a partir de los metadatos del ResultSet
	 * de una consulta a base de datos
	 */
	private void estableceColumnasModelo() {
		try {
			// Obtiene metadatos.
			ResultSetMetaData metaDatos = rsUsuarios.getMetaData();

			// Número total de columnas.
			int numCol = metaDatos.getColumnCount();

			// Etiqueta de cada columna.
			Object[] etiquetas = new Object[numCol];
			for (int i = 0; i < numCol; i++) {
				etiquetas[i] = metaDatos.getColumnLabel(i + 1);
			}

			// Incorpora array de etiquetas en el TableModel.
			((DefaultTableModel) tmUsuarios).setColumnIdentifiers(etiquetas);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Borra todas las filas del TableModel
	 * @param tm - El TableModel a vaciar
	 */
	private void borraFilasModelo() {
		while (tmUsuarios.getRowCount() > 0)
			((DefaultTableModel) tmUsuarios).removeRow(0);
	}

	/**
	 * Replica en el TableModel las filas del ResultSet
	 */
	private void rellenaFilasModelo() {
		Object[] datosFila = new Object[tmUsuarios.getColumnCount()];
		// Para cada fila en el ResultSet de la consulta.
		try {
			while (rsUsuarios.next()) {
				// Se replica y añade la fila en el TableModel.
				for (int i = 0; i < tmUsuarios.getColumnCount(); i++)
					datosFila[i] = rsUsuarios.getObject(i + 1);
				((DefaultTableModel) tmUsuarios).addRow(datosFila);
			}
			rsUsuarios.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Devuelve el ResultSet creado dentro del método.
	public ResultSet leerResultSet() throws SQLException {
		// Crea un Statement (sentencia-sql) en la conexión a la BD, para
		// realizar la consulta.

		// Convertimos el ResultSet, en desplazable y actualizable.
		java.sql.Statement stm = db.createStatement(
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

		// Ejecuta la consulta, y los resultados quedan almacenados en el
		// ResultSet.
		ResultSet rsUsuarios = stm
				.executeQuery("SELECT idUsr, nif, nombre, apellidos, correo, domicilio, fechaAlta, rol FROM usuarios");
		return rsUsuarios;
	}


	//MÉTODOS DAO Usuarios
	/** Devuelve listado completo de usuarios.
	 *  @return texto con el volcado de todos los usuarios.
	 */
	public String listarDatos() {
		return obtenerTodos().toString();
	}

	/**
	 * Obtiene el usuario buscado dado su idUsr. 
	 * Si no existe devuelve null.
	 * @param id del usuario a obtener.
	 * @return (Usuario) buscado. 
	 */	
	@Override
	public Usuario obtener(String idUsr) {
		// Se realiza la consulta y los resultados quedan en el ResultSet
		try {		
			rsUsuarios = sentenciaUsr.executeQuery("SELECT * FROM usuarios WHERE idUsr = '" + idUsr + "'");

			// Establece columnas y etiquetas
			estableceColumnasModelo();

			// Borrado previo de filas
			borraFilasModelo();

			// Volcado desde el resulSet
			rellenaFilasModelo();

			// Actualiza buffer de objetos.
			sincronizarBufferObjetos();
			if (bufferObjetos.size() > 0) {
				return bufferObjetos.get(0);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Obtiene el usuario buscado dado un objeto. 
	 * Si no existe devuelve null.
	 * @param Usuario a obtener.
	 * @return (Usuario) buscado. 
	 */	
	@Override
	public Object obtener(Object obj) {
		return obtener(((Usuario) obj).getIdUsr());
	}

	/**
	 * Regenera lista de los objetos procesando el tableModel. 
	 */	
	private void sincronizarBufferObjetos() {
		bufferObjetos.clear();
		for (int i=0;i < tmUsuarios.getRowCount(); i++) {
			Nif nif = new Nif((String) tmUsuarios.getValueAt(i, 1));
			String nombre = (String) tmUsuarios.getValueAt(i, 2);
			String apellidos = (String) tmUsuarios.getValueAt(i, 3);
			Direccion domicilio = new Direccion((String) tmUsuarios.getValueAt(i, 4));
			Correo correo = new Correo((String) tmUsuarios.getValueAt(i, 5));
			java.sql.Date fechaSQL = (Date) tmUsuarios.getValueAt(i, 6);
			Fecha fechaNacimiento = new Fecha(fechaSQL.getYear(), fechaSQL.getMonth(), fechaSQL.getDay());
			fechaSQL = (Date) tmUsuarios.getValueAt(i, 7);
			Fecha fechaAlta = new Fecha(fechaSQL.getYear(), fechaSQL.getMonth(), fechaSQL.getDay());
			Contraseña claveAcceso = new Contraseña((String) tmUsuarios.getValueAt(i, 8));

			RolUsuario rol = null;
			switch ((String)tmUsuarios.getValueAt(i, 9)) {
			case "INVITADO":  
				rol = RolUsuario.INVITADO;
				break;
			case "NORMAL":
				rol = RolUsuario.NORMAL;
				break;
			case "ADMINISTRADOR":
				rol = RolUsuario.ADMINISTRADOR;
				break;
			}

			// Genera y guarda objeto
			bufferObjetos.add(new Usuario(nif, nombre, apellidos, domicilio, correo, 
					fechaNacimiento, fechaAlta, claveAcceso, rol));
		}
	}

	/**
	 * Obtiene todos los usuarios almacenados. 
	 * Si no hay resultados devuelve null.
	 * @param idUsr a obtener.
	 * @return (DefaultTableModel) result obtenido.
	 */	
	public List<Usuario> obtenerTodos() {
		try {
			// Se crea un Statement (declaración) en la conexión a la BD, 
			// para realizar la consulta
			java.sql.Statement s;
			s = db.createStatement();

			// Se realiza la consulta y los resultados quedan en el ResultSet
			rsUsuarios = s.executeQuery("SELECT * FROM usuarios");

			// Establece columnas y etiquetas
			this.estableceColumnasModelo();

			// Borrado previo de filas
			this.borraFilasModelo();

			// Volcado desde el resulSet
			this.rellenaFilasModelo();

			// Actualiza buffer de objetos.
			sincronizarBufferObjetos();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return bufferObjetos;
	}

	/**
	 * Da de alta un nuevo usuario en la base de datos.
	 * @param usr, el objeto a dar de alta.
	 * @throws DatosException 
	 */	
	@Override
	public void alta(Object obj) throws DatosException {	
		Usuario usr = (Usuario) obj;
		if (obtener(usr) == null) {
			almacenar(usr);
			registrarEquivalenciaId(usr);
		}
		else {
			throw new DatosException("ALTA: El Usuario " + usr.getIdUsr() + " ya existe...");
		}
	}

	/**
	 * almacena usuario en la base de datos.
	 * @param usr, el objeto a procesar.
	 * @throws DatosException 
	 */
	private void almacenar(Usuario usr) {
		try {
			ResultSet rsUsr = null;
			// Se realiza la consulta y los resultados quedan en el ResultSet
			rsUsr = sentenciaUsr.executeQuery("SELECT * FROM usuarios");
			rsUsr.moveToInsertRow();
			rsUsr.updateString("idUsr", ((Usuario)usr).getIdUsr());
			rsUsr.updateString("nif", ((Usuario)usr).getNif().toString());
			rsUsr.updateString("nombre", ((Usuario)usr).getNombre());
			rsUsr.updateString("apellidos", ((Usuario)usr).getApellidos());
			rsUsr.updateString("domicilio", ((Usuario)usr).getDomicilio().toString());
			rsUsr.updateString("correo", ((Usuario)usr).getCorreo().toString());
			rsUsr.updateDate("fechaNacimiento", new java.sql.Date(
					((Usuario)usr).getFechaNacimiento().getAño(),
					((Usuario)usr).getFechaNacimiento().getMes(),
					((Usuario)usr).getFechaNacimiento().getDia()
					));
			rsUsr.updateDate("fechaAlta",  new java.sql.Date(
					((Usuario)usr).getFechaAlta().getAño(),
					((Usuario)usr).getFechaAlta().getMes(),
					((Usuario)usr).getFechaAlta().getDia()
					));
			rsUsr.updateString("claveAcceso", ((Usuario)usr).getIdUsr());
			rsUsr.updateString("rol", ((Usuario)usr).getRol().toString());
			rsUsr.insertRow();
			rsUsr.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Da de baja un usuario de la base de datos.
	 * @param idUsr, id del usuario a dar de baja.
	 * @throws DatosException
	 */
	@Override
	public Object baja(String idUsr) throws DatosException  {
		Usuario usr = obtener(idUsr);
		if (usr != null) {
			borrarEquivalenciaId(usr.getIdUsr());
			borrar(usr);
		}
		else {
			throw new DatosException("BAJA: El Usuario " + usr.getIdUsr() + " no existe...");
		}
		return usr;
	}

	/**
	 * Elimina el usuario.
	 * @param usuario - el usuario para eliminar.
	 */
	private void borrar(Usuario usr) {
		bufferObjetos.remove(usr);
		try {
			sentenciaId.executeQuery("DELETE FROM usuarios WHERE idUsr = '" + usr.getIdUsr() + "'");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Modifica un usuario de la base de datos.
	 * @param usr, objeto Usuario con los valores a cambiar.
	 * @throws DatosException 
	 */
	@Override
	public void actualizar(Object usr) throws DatosException {

	}

	//GESTION equivalencias id
	/**
	 * Registra las equivalencias de nif y correo para un idUsr.
	 * @param usuario
	 */
	private void registrarEquivalenciaId(Usuario usr) {
		try {
			ResultSet rsEquival = null;
			// Se realiza la consulta y los resultados quedan en el ResultSet
			rsEquival = sentenciaId.executeQuery("SELECT * FROM equivalid");
			rsEquival.moveToInsertRow();
			rsEquival.updateString("equival", usr.getNif().toString());
			rsEquival.updateString("idUsr", usr.getIdUsr().toString());
			rsEquival.insertRow();
			rsEquival.beforeFirst();
			rsEquival.moveToInsertRow();
			rsEquival.updateString("equival", usr.getCorreo().toString());
			rsEquival.updateString("idUsr", usr.getIdUsr().toString());
			rsEquival.insertRow();
			rsEquival.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Obtiene el idUsr usado internamente a partir de otro equivalente.
	 * @param id - la clave alternativa. 
	 * @return - El idUsr equivalente.
	 */
	public String obtenerEquivalencia(String id) {
		// Se realiza la consulta y los resultados quedan en el ResultSet
		try {		
			ResultSet rsEquival = sentenciaUsr.executeQuery("SELECT * FROM equivalid WHERE equival = '" + id + "'");
			return rsEquival.getString(0);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Elimina las equivalencias de nif y correo para un idUsr.
	 * @param usuario - el usuario para eliminar sus equivalencias de idUsr.
	 */
	private void borrarEquivalenciaId(String idUsr) {
		try {
			sentenciaId.executeQuery("DELETE FROM equivalid WHERE idUsr = '" + idUsr + "'");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void cerrar() {
		try {
			sentenciaUsr.close();
			sentenciaId.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}


} // class
