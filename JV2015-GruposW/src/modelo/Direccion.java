package modelo;

import java.io.Serializable;

import util.Formato;

public class Direccion implements Serializable {
	private String codigoPostal;
	private String via;
	private String numero;
	private String poblacion;
	private String pais;
	
	public Direccion(String codigoPostal, String via, String numero, 
						String poblacion, String pais) {
		setCodigoPostal(codigoPostal);
		setVia(via);
		setNumero(numero);
		setPoblacion(poblacion);
		setPais(pais);
	}

	public Direccion() {
		this("30000", "Via", "0", "Murcia", "España");
	}
	
	public Direccion(Direccion direccion) {
		this(direccion.codigoPostal, direccion.via, direccion.numero, 
				direccion.poblacion, direccion.pais);
	}
	
	public void setCodigoPostal(String codigoPostal) {
		assert esValidoCodigoPostal(codigoPostal);
		this.codigoPostal = codigoPostal;
	}

	/**
	 * Comprueba validez de un código Postal.
	 * @param codigoPostal.
	 * @return true si cumple.
	 */
	public static boolean esValidoCodigoPostal(String codigoPostal) {
		if (codigoPostal != null
				&& util.Formato.validar(codigoPostal, Formato.PATRON_CP) 
				&& codigoPostalAutentico(codigoPostal)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba que existe el código postal.
	 * @param codigoPostal.
	 * @return true si cumple.
	 */
	private static boolean codigoPostalAutentico(String codigoPostal) {
		// Comprueba que el codigo postal no es falso. 
		//--Pendiente--
		return true;
	}

	public void setVia(String via) {
		assert esValidaVia(via);
		this.via = via;	
	}

	/**
	 * Comprueba validez de la vía pública.
	 * @param via.
	 * @return true si cumple.
	 */
	public static boolean esValidaVia(String via) {
		if (via != null
				&& util.Formato.validar(via, Formato.PATRON_TOPONIMO)
				&& viaAutentica(via)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba que existe la vía pública.
	 * @param via.
	 * @return true si cumple.
	 */
	private static boolean viaAutentica(String via) {
		// Comprueba que la via no es falsa.
		//--Pendiente--
		return true;
	}
	
	public void setNumero(String numero) {
		assert esValidoNumero(numero);
		this.numero = numero;
	}
	
	/**
	 * Comprueba validez de la vía pública.
	 * @param via.
	 * @return true si cumple.
	 */
	public static boolean esValidoNumero(String numero) {
		if (numero != null
				&& util.Formato.validar(numero, Formato.PATRON_NUMERO_POSTAL)) {
			return true;
		}
		return false;
	}
	
	public void setPoblacion(String poblacion) {
		assert esValidapoblacion(poblacion);
		this.poblacion = poblacion;
	}

	/**
	 * Comprueba validez de la poblacion.
	 * @param poblacion.
	 * @return true si cumple.
	 */
	public static boolean esValidapoblacion(String poblacion) {
		if (poblacion != null
				&& util.Formato.validar(poblacion, Formato.PATRON_TOPONIMO)
				&& poblacionAutentica(poblacion)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba que existe la población.
	 * @param poblacion.
	 * @return true si cumple.
	 */
	private static boolean poblacionAutentica(String poblacion) {
		// Comprueba que la población no es falsa.
		//--Pendiente--
		return true;
	}
	
	public void setPais(String pais) {
		assert esValidoPais(pais);
		this.pais = pais;
	}

	/**
	 * Comprueba validez del pais.
	 * @param pais.
	 * @return true si cumple.
	 */
	public static boolean esValidoPais(String pais) {
		if (pais != null
				&& util.Formato.validar(pais, Formato.PATRON_TOPONIMO)
				&& paisAutentico(pais)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba que existe el pais.
	 * @param pais.
	 * @return true si cumple.
	 */
	private static boolean paisAutentico(String pais) {
		// Comprueba que el pais existe.
		//--Pendiente--
		return true;
	}

	/**
	 * @return the codigoPostal
	 */
	public String getCodigoPostal() {
		return codigoPostal;
	}

	/**
	 * @return the via
	 */
	public String getVia() {
		return via;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @return the poblacion
	 */
	public String getPoblacion() {
		return poblacion;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	@Override
	public String toString() {
		return codigoPostal + ", " + via + ", " + numero
				+ ", " + poblacion + ", " + pais;
	}
	
} // class
