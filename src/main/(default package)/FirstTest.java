import data.Data;

import clustering.HierachicalClusterMiner;
import clustering.exceptions.InvalidDepthException;
import clustering.exceptions.InvalidSizeException;
import distance.*;
import util.Keyboard;

public class FirstTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Data data = new Data("exampleTab");
		System.out.println(data);
		int k = 5;
		System.out.print("Inserire profondità del dendrogramma: ");
		k = Keyboard.readInt();

		// serialization

		System.out.print("inserire il nome del file dove salvare l'oggetto: ");
		String fileName = Keyboard.readString();
		HierachicalClusterMiner clustering = new HierachicalClusterMiner(k);

		try {
			clustering.save(fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
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
