package clustering;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * classe che rappresenta il cluster dei vettori Example, salvando
 * all'interno di clusteredData[] le posizioni dei vettori Example all'interno
 * del vettore Data che fanno parte del Cluster.
 */
public class Cluster implements Iterable<Integer>, Cloneable {

	/**
	 * Collezione delle posizioni dei vettori Example nella classe
	 * Data, che rappresentano i vettori Example contenuti nel Cluster
	 */
	private Set<Integer> clusteredData = new TreeSet<>();

	/**
	 * Restituisce un iteratore per i dati raggruppati.
	 * 
	 * @return un iteratore per l'insieme di dati raggruppati
	 */
	@Override
	public Iterator<Integer> iterator() {
		return clusteredData.iterator();
	}

	/**
	 * aggiunge un vettore Example al cluster.
	 * 
	 * @param id posizione del vettore Example.
	 */
	void addData(int id) {
		clusteredData.add(id);
	}

	/**
	 * restituisce il numero di elementi del Cluster.
	 * 
	 * @return il numero di elementi del Cluster.
	 */
	public int getSize() {
		return clusteredData.size();
	}

	/**
	 * restituisce la posizione del vettore Example all'interno di Data.
	 * 
	 * @param i indice di un vettore Example memorizzato in Cluster.
	 * @return la posizione del vettore Example all'interno di Data.
	 */

	// public int getElement(int i) {
	// return clusteredData[i];
	// }

	/**
	 * crea una copia del cluster corrente.
	 * 
	 * @return hard copy dell'oggetto Cluster.
	 */
	@Override
	public Object clone() {
		Cluster newCluster = new Cluster();
		for (Integer index : this) {
			newCluster.addData(index);
		}
		return newCluster;
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
		for (Integer index : this)
			newC.addData(index);
		for (Integer index : c)
			newC.addData(index);
		return newC;

	}

	/**
	 * restituisce la stringa del vettore clusteredData.
	 * 
	 * @return stringa delle posizioni dei vettori Example contenuti nel Cluster.
	 */
	public String toString() {
		String str = "";
		Iterator<Integer> iterator = clusteredData.iterator();
		while (iterator.hasNext()) {
			str += iterator.next();
			if (iterator.hasNext()) {
				str += ",";
			}
		}
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

		for (Integer index : clusteredData) {
			str += "<" + data.getExample(index) + ">";
		}

		return str;

	}

}
