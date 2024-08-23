/**
 * classe che rappresente un insieme di Cluster.
 */
class ClusterSet {

	private Cluster C[]; // vettore di Cluster.
	private int lastClusterIndex = 0; // indice dell'ultimo Cluster aggiunto.

	ClusterSet(int k) {
		C = new Cluster[k];
	}

	/**
	 * aggiunge un cluster a ClusterSet.
	 * 
	 * @param c cluster da agginugere.
	 */
	private void add(Cluster c) {
		for (int j = 0; j < lastClusterIndex; j++)
			if (c == C[j]) // to avoid duplicates
				return;
		C[lastClusterIndex] = c;
		lastClusterIndex++;
	}

	Cluster get(int i) {
		return C[i];
	}

	/**
	 * determina la coppia di cluster più simili e li fonde in unico cluster; crea
	 * una nuova istanza di ClusterSet che contiene tutti i cluster dell’oggetto
	 * this a meno dei due cluster fusi al posto dei quali inserisce il cluster
	 * risultante dalla fusione
	 * 
	 * (nota bene l’oggetto ClusterSet risultante memorizza un numero di cluster che
	 * è pari al numero di cluster memorizzato nell’oggetto this meno -1)
	 * 
	 * @param distance oggetto per il calcolo della distanza tra cluster
	 * @param data     oggetto istanza che rappresenta il dataset in cui si sta
	 *                 calcolando l’oggetto istanza di ClusterSet
	 * @return nuova istanza di ClusterSet
	 */
	ClusterSet mergeClosestClusters(ClusterDistance distance, Data data) {
		double minDistance = Double.MAX_VALUE;
		int firstCluster = 0, secondCluster = 0;
		for (int i = 0; i < lastClusterIndex; i++) {
			for (int j = 0; j < lastClusterIndex; j++) {
				if (i == j) {
					continue;
				}
				Double d = distance.distance(this.get(i), this.get(j), data);
				if (minDistance > d) {
					minDistance = d;
					firstCluster = i;
					secondCluster = j;
				}
			}
		}

		ClusterSet newClusterSet = new ClusterSet(lastClusterIndex - 1);
		for (int i = 0; i < lastClusterIndex; i++) {
			if (i != firstCluster || i != secondCluster) {
				newClusterSet.add(this.get(i));
			}
		}
		newClusterSet.add(this.get(firstCluster).mergeCluster(this.get(secondCluster)));

		return newClusterSet;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < C.length; i++) {
			if (C[i] != null) {
				str += "cluster" + i + ":" + C[i] + "\n";

			}
		}
		return str;

	}

	String toString(Data data) {
		String str = "";
		for (int i = 0; i < C.length; i++) {
			if (C[i] != null) {
				str += "cluster" + i + ":" + C[i].toString(data) + "\n";

			}
		}
		return str;

	}

}
