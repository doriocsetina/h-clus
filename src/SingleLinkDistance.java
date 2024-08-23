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

		for (int i = 0; i < c1.getSize(); i++) {
			Example e1 = d.getExample(c1.getElement(i));
			for (int j = 0; j < c2.getSize(); j++) {
				double distance = e1.distance(d.getExample(c2.getElement(j)));
				if (distance < min)
					min = distance;
			}
		}
		return min;
	}
}
