package clustering;
import data.Data;
import distance.ClusterDistance;

/**
 * classe responsabile per l'implementazione dell'algoritmo di clustering
 * gerarchico.
 */

public class HierachicalClusterMiner {

	private Dendrogram dendrogram; // dendrogramma che rappresenta la struttura gerarchicha dei cluster.

	/**
	 * costruttore che inizializza il dendrogramma del miner.
	 * 
	 * @param depth profondità del dendrogramma.
	 */
	public HierachicalClusterMiner(int depth) {
		dendrogram = new Dendrogram(depth);

	}

	/**
	 * crea il livello base (livello 0) del dendrogramma che contiene l’istanze di
	 * ClusterSet che rappresenta ogni esempio in un cluster separato; per tutti i
	 * livelli successivi del dentrogramma (level>=1 e level <
	 * dendrogram.getDepth()) costruisce l’istanza di ClusterSet che realizza la
	 * fusione dei due cluster più vicini nella istanza del ClusterSet memorizzata
	 * al livello level-1 del dendrogramma (usare mergeClosestClusters di
	 * ClusterSet);
	 * 
	 * memorizza l’istanza di ClusterSet ottenuta per fusione nel livello level del
	 * dendrogramma.
	 * 
	 * @param data
	 * @param distance
	 */
	public void mine(Data data, ClusterDistance distance) {
		ClusterSet firstClusterSet = new ClusterSet(data.getNumberOfExamples());

		// crea livello 0 dove ogni cluster è un singolo dato.
		for (int i = 0; i < data.getNumberOfExamples(); i++) {
			Cluster singleCluster = new Cluster();
			singleCluster.addData(i);
			firstClusterSet.add(singleCluster);
		}

		dendrogram.setClusterSet(firstClusterSet, 0);

		// per ogni livello da 1 a depth forma un nuovo ClusterSet a partire da
		// dendogram.getClusterSet(i - 1).
		for (int i = 1; i < dendrogram.getDepth(); i++) {
			ClusterSet newClusterSet = dendrogram.getClusterSet(i - 1).mergeClosestClusters(distance, data);
			dendrogram.setClusterSet(newClusterSet, i);
		}
	}

	/**
	 * fornisce una rappresentazione stringa dell'oggetto Dendrogram contenuta in
	 * HierachicalClusterMiner.
	 *
	 * @return una stringa che rappresenta l'oggetto Dendrogram.
	 */
	public String toString() {
		return dendrogram.toString();
	}

	/**
	 * fornisce una rappresentazione stringa dell'oggetto Dendrogram contenuta in
	 * HierachicalClusterMiner, dove i cluster contengono esplicitamente i vettori
	 * Example.
	 * 
	 * @param data vettore Data dove sono contenuti i vettori Example.
	 * @return na stringa che rappresenta l'oggetto Dendrogram.
	 */
	public String toString(Data data) {
		return dendrogram.toString(data);
	}

}
