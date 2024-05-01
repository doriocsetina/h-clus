
class HierachicalClusterMiner {

	private Dendrogram dendrogram;

	HierachicalClusterMiner(int depth) {
		dendrogram = new Dendrogram(depth);

	}

	public String toString() {
		return dendrogram.toString();
	}

	String toString(Data data) {
		return dendrogram.toString(data);
	}

}
