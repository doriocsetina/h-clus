package server.distance;

import server.clustering.Cluster;
import server.data.Data;

/**
 * L'interfaccia ClusterDistance fornisce un metodo per calcolare la distanza
 * tra due cluster.
 */
public interface ClusterDistance {

    /**
     * Calcola e restituisce la distanza tra due cluster.
     *
     * @param c1 il primo cluster.
     * @param c2 il secondo cluster.
     * @param d  i dati associati ai cluster.
     * @return la distanza tra i due cluster.
     */
    double distance(Cluster c1, Cluster c2, Data d);
}