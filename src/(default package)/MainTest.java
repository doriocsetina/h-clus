import data.Data;

import clustering.HierachicalClusterMiner;
import clustering.exceptions.InvalidDepthException;
import clustering.exceptions.InvalidSizeException;
import distance.*;
import util.Keyboard;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Data data = new Data();
		System.out.println(data);
		int k = 5;
		System.out.println("Inserire profondità del dendrogramma: ");
		k = Keyboard.readInt();
		HierachicalClusterMiner clustering = new HierachicalClusterMiner(k);

		System.out.println("Single link distance");
		ClusterDistance distance = new SingleLinkDistance();
		try {
			double[][] distancematrix = data.distance();
			System.out.println("Distance matrix:\n");
			for (int i = 0; i < distancematrix.length; i++) {
				for (int j = 0; j < distancematrix.length; j++)
					System.out.print(distancematrix[i][j] + "\t");
				System.out.println("");
			}
		} catch (InvalidSizeException e) {
			System.err.println(e.getMessage());
			return;
		}

		try {
			clustering.mine(data, distance);
			System.out.println(clustering);
			System.out.println(clustering.toString(data));

		} catch (InvalidDepthException e) {
			System.err.println(e.getMessage());
		}

		System.out.println("Average link distance");
		distance = new AverageLinkDistance();
		try {
			clustering.mine(data, distance);
			System.out.println(clustering);
			System.out.println(clustering.toString(data));
		} catch (InvalidDepthException e) {
			System.err.println(e.getMessage());
		}

	}

}
