package clustering;

import data.Data;

/**
 * classe che rappresenta il cluster dei vettori Example, salvando
 * all'interno di clusteredData[] le posizioni dei vettori Example all'interno
 * del vettore Data che fanno parte del Cluster.
 */
public class Cluster {

	/**
	 * Collezione delle posizioni dei vettori Example nella classe
	 * Data, che rappresentano i vettori Example contenuti nel Cluster
	 */
	private Integer clusteredData[] = new Integer[0];

	/**
	 * aggiunge un vettore Example al cluster.
	 * 
	 * @param id posizione del vettore Example.
	 */
	void addData(int id) {
		for (int i = 0; i < clusteredData.length; i++)
			if (id == clusteredData[i])
				return;
		Integer clusteredDataTemp[] = new Integer[clusteredData.length + 1];
		System.arraycopy(clusteredData, 0, clusteredDataTemp, 0, clusteredData.length);
		clusteredData = clusteredDataTemp;
		clusteredData[clusteredData.length - 1] = id;
	}

	/**
	 * restituisce il numero di elementi del Cluster.
	 * 
	 * @return il numero di elementi del Cluster.
	 */
	public int getSize() {
		return clusteredData.length;
	}

	/**
	 * restituisce la posizione del vettore Example all'interno di Data.
	 * 
	 * @param i indice di un vettore Example memorizzato in Cluster.
	 * @return la posizione del vettore Example all'interno di Data.
	 */
	public int getElement(int i) {
		return clusteredData[i];
	}

	/**
	 * crea una copia del cluster corrente.
	 * 
	 * @return hard copy dell'oggetto Cluster.
	 */
	Cluster createACopy() {
		Cluster copyC = new Cluster();
		for (int i = 0; i < getSize(); i++)
			copyC.addData(clusteredData[i]);
		return copyC;
	}

	/**
	 * crea un nuovo Cluster che è la fusione dei due Cluster pre-esistenti.
	 * 
	 * @param c Cluster che verrà fuso a this.
	 * @return un nuovo Cluster contente gli oggetti di entrambi i Cluster
	 *         precedenti.
	 */
	Cluster mergeCluster(Cluster c) {
		Cluster newC = new Cluster();
		for (int i = 0; i < getSize(); i++)
			newC.addData(clusteredData[i]);
		for (int i = 0; i < c.getSize(); i++)
			newC.addData(c.clusteredData[i]);
		return newC;

	}

	/**
	 * restituisce la stringa del vettore clusteredData.
	 * 
	 * @return stringa delle posizioni dei vettori Example contenuti nel Cluster.
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < clusteredData.length - 1; i++)
			str += clusteredData[i] + ",";
		str += clusteredData[clusteredData.length - 1];
		return str;
	}

	/**
	 * restituisce la stringa dei vettori contenuti nel Cluster.
	 * 
	 * @param data vettore Data che contiene i vettori Example.
	 * @return stringa che rappresenta interamente i vettori Example contenuti nel
	 *         Cluster.
	 */
	String toString(Data data) {
		String str = "";

		for (int i = 0; i < clusteredData.length; i++)
			str += "<" + data.getExample(clusteredData[i]) + ">";

		return str;

	}

}
