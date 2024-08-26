package distance;

import clustering.Cluster;
import data.Data;

/**
 * l'interfaccia ClusterDistance fornisce un metodo per calcolare la distanza
 * tra due cluster.
 */
public interface ClusterDistance {

    /**
     * calcola e restituisce la distanza tra due cluster.
     *
     * @param c1 il primo cluster.
     * @param c2 il secondo cluster.
     * @param d  i dati associati ai cluster.
     * @return la distanza tra i due cluster.
     */
    double distance(Cluster c1, Cluster c2, Data d);
}