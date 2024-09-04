package server.clustering;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import server.clustering.exceptions.InvalidDepthException;
import server.data.Data;
import server.distance.ClusterDistance;

/**
 * Classe responsabile per l'implementazione dell'algoritmo di clustering
 * gerarchico.
 * 
 * Contiene i metodi load e save che si occupano di serializzare l'istanza
 * dell'oggetto;
 * 
 * Contiene il metodo mine, che crea il Dendrogramma ed opera il mining,
 * popolandolo ciascun livello fino alla profondità specificata.
 */

public class HierachicalClusterMiner implements Serializable {

	private Dendrogram dendrogram; // dendrogramma che rappresenta la struttura gerarchicha dei cluster.
	private String dataChecksum; // checksum dei dati su cui è stata operata l'operazione mine

	/**
	 * Costruttore che inizializza il dendrogramma del miner.
	 * 
	 * @param depth profondità del dendrogramma.
	 */
	public HierachicalClusterMiner(int depth) {
		dendrogram = new Dendrogram(depth);

	}

	/**
	 * Carica a partire dal nome del file un'istanza di HierarchicalClusterMiner
	 * serializzata su disco.
	 * 
	 * @param filename nome del file serializzato.
	 * @return istanza di HierarchicalClusterMiner serializzata caricata dalla
	 *         funzione.
	 * @throws FileNotFoundException  lanciata se nel path specificato non è stato
	 *                                trovato nessun file, o il file non è apribile
	 *                                in nessun modo.
	 * @throws IOException            lanciata nel caso di errore durante la lettura
	 *                                del file.
	 * @throws ClassNotFoundException lanciata nel caso in cui l'oggetto
	 *                                serializzato che si vuole caricare non è
	 *                                istanza di nessuna classe.
	 */
	public static HierachicalClusterMiner load(String filename)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream inFile = new FileInputStream(filename);
		try (ObjectInputStream inStream = new ObjectInputStream(inFile)) {
			return (HierachicalClusterMiner) inStream.readObject();
		}
	}

	/**
	 * Carica un'istanza di HierarchicalClusterMiner a partire dallo stream ricevuto
	 * in input.
	 * 
	 * @param inFile il file serializzato ricevuto.
	 * @return istanza di HierarchicalClusterMiner serializzata caricata dalla
	 *         funzione.
	 * @throws FileNotFoundException  lanciata se nel path specificato non è stato
	 *                                trovato nessun file, o il file non è apribile
	 *                                in nessun modo.
	 * @throws IOException            lanciata nel caso di errore durante la lettura
	 *                                del file.
	 * @throws ClassNotFoundException lanciata nel caso in cui l'oggetto
	 *                                serializzato che si vuole caricare non è
	 *                                istanza di nessuna classe.
	 */
	public static HierachicalClusterMiner loadFromFile(FileInputStream inFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		try (ObjectInputStream inStream = new ObjectInputStream(inFile)) {
			return (HierachicalClusterMiner) inStream.readObject();
		}
	}

	/**
	 * Salva l'istanza corrente di HierarchicalClusterMiner serializzandola e
	 * scrivendola su disco al percorso specificato
	 * 
	 * @param fileName il nome con cui si desidera serializzare la classe.
	 * @throws FileNotFoundException lanciata nel caso non è possibile creare il
	 *                               file con questo nome.
	 * @throws IOException           lanciata nel casi di errore durante la
	 *                               scrittura del file.
	 */
	public void save(String fileName) throws FileNotFoundException, IOException {
		FileOutputStream outFile = new FileOutputStream(fileName);
		try (ObjectOutputStream outStream = new ObjectOutputStream(outFile)) {
			outStream.writeObject(this);
		}

	}

	/**
	 * Crea il livello base (livello 0) del dendrogramma che contiene l’istanze di
	 * ClusterSet che rappresenta ogni esempio in un cluster separato; per tutti i
	 * livelli successivi del dentrogramma (level>=1 e level <
	 * dendrogram.getDepth()) costruisce l’istanza di ClusterSet che realizza la
	 * fusione dei due cluster più vicini nella istanza del ClusterSet memorizzata
	 * al livello level-1 del dendrogramma (usare mergeClosestClusters di
	 * ClusterSet);
	 * 
	 * Memorizza l’istanza di ClusterSet ottenuta per fusione nel livello level del
	 * dendrogramma.
	 * 
	 * @param data     i dati su cui operare il mining.
	 * @param distance il tipo di distanza in base al quale eseguire il merge tra i
	 *                 cluster.
	 * @throws InvalidDepthException se il numero dei vettori Example contenuti
	 *                               nella classe Data risulta essere maggiore della
	 *                               profondità del Dendrogramma.
	 */
	public void mine(Data data, ClusterDistance distance) throws InvalidDepthException {
		int totalExamples = data.getNumberOfExamples();
		if (totalExamples < dendrogram.getDepth()) {
			throw new InvalidDepthException(dendrogram.getDepth(), totalExamples);
		}
		ClusterSet firstClusterSet = new ClusterSet(totalExamples);

		// crea livello 0 dove ogni cluster è un singolo dato.ss
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
		dataChecksum = data.generateChecksum();
	}

	/**
	 * Verifica che i dati usati nella corrente istanza di HierarchicalClusterMiner
	 * siano gli stessi dati in input tramite il controllo del checksum dell'istanza
	 * Data.
	 * 
	 * @param data i dati che si desidera verificare.
	 * @return boolean di valore true se i dati in input sono gli stessi su cui è
	 *         stato applicato il miner.
	 */
	public boolean validateData(Data data) {
		return data.generateChecksum().equals(dataChecksum);
	}

	/**
	 * Fornisce una rappresentazione stringa dell'oggetto Dendrogram contenuta in
	 * HierachicalClusterMiner.
	 *
	 * @return la stringa che rappresenta l'oggetto Dendrogram.
	 */
	public String toString() {
		return dendrogram.toString();
	}

	/**
	 * Fornisce una rappresentazione stringa dell'oggetto Dendrogram contenuta in
	 * HierachicalClusterMiner, dove i cluster contengono esplicitamente i vettori
	 * Example.
	 * 
	 * @param data vettore Data dove sono contenuti i vettori Example.
	 * @return la stringa che rappresenta l'oggetto Dendrogram.
	 */
	public String toString(Data data) {
		if (validateData(data))
			return dendrogram.toString(data);
		return "ERRORE: il dendrogramma non è stato costruito su questi dati.";
	}

}
