package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

/**
 * implementazione della interfaccia ClusterDistance che utilizza il metodo
 * single-link.
 * nel metodo Single Link, la distanza tra due cluster è definita come la
 * distanza minima tra qualsiasi coppia di esempi in cui uno proviene da ciascun
 * cluster.
 */
public class SingleLinkDistance implements ClusterDistance {

	/**
	 * calcola e restituisce la distanza minima tra qualsiasi coppia di esempi in
	 * cui uno proviene da ciascun cluster.
	 *
	 * @param c1 il primo cluster.
	 * @param c2 il secondo cluster.
	 * @param d  vettore Data contenente i vettori Example.
	 * @return la distanza minima tra qualsiasi coppia di esempi in cui uno proviene
	 *         da ciascun cluster.
	 */
	public double distance(Cluster c1, Cluster c2, Data d) {

		double min = Double.MAX_VALUE;

		for (Integer index1 : c1) {
			Example e1 = d.getExample(index1);
			for (Integer index2 : c2) {
				double distance = e1.distance(d.getExample(index2));
				if (distance < min)
					min = distance;
			}
		}

		return min;
	}
}
