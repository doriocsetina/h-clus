
class Cluster {

	private Integer clusteredData[] = new Integer[0]; // Collezione delle posizioni dei vettori Example nella classe
														// Data, che rappresentano i vettori Example contenuti nel
														// Cluster

	/**
	 * aggiunge un vettore Example al cluster
	 * 
	 * @param id posizione del vettore Example
	 */
	void addData(int id) {
		// controllo duplicati
		for (int i = 0; i < clusteredData.length; i++)
			if (id == clusteredData[i])
				return;
		Integer clusteredDataTemp[] = new Integer[clusteredData.length + 1];
		System.arraycopy(clusteredData, 0, clusteredDataTemp, 0, clusteredData.length);
		clusteredData = clusteredDataTemp;
		clusteredData[clusteredData.length - 1] = id;
	}

	/**
	 * restituisce la dimensione del Cluster
	 * 
	 * @return dimensione del Cluster
	 */
	int getSize() {
		return clusteredData.length;
	}

	/**
	 * restituisce la posizione del vettore Example all'interno di Data
	 * 
	 * @param i indice di un vettore Example memorizzato in Cluster
	 * @return la posizione del vettore Example all'interno di Data
	 */
	int getElement(int i) {
		return clusteredData[i];
	}

	/**
	 * crea una copia del cluster corrente
	 * 
	 * @return hard copy dell'oggetto Cluster
	 */
	Cluster createACopy() {
		Cluster copyC = new Cluster();
		for (int i = 0; i < getSize(); i++)
			copyC.addData(clusteredData[i]);
		return copyC;
	}

	/**
	 * crea un nuovo Cluster che è la fusione dei due Cluster pre-esistenti
	 * 
	 * @param c Cluster che verrà fuso a this
	 * @return Cluster fusione di c e this
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
	 * restituisce la stringa del vettore clusteredData
	 * 
	 * @return stringa delle posizioni dei vettori Example contenuti nel Cluster
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < clusteredData.length - 1; i++)
			str += clusteredData[i] + ",";
		str += clusteredData[clusteredData.length - 1];
		return str;
	}

	/**
	 * restituisce la stringa dei vettori contenuti nel Cluster
	 * 
	 * @param data vettore Data che contiene i vettori Example
	 * @return stringa che rappresenta interamente i vettori Example contenuti nel Cluster
	 */
	String toString(Data data) {
		String str = "";

		for (int i = 0; i < clusteredData.length; i++)
			str += "<" + data.getExample(clusteredData[i]) + ">";

		return str;

	}

}
