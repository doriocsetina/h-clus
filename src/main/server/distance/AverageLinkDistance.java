package server.distance;

import server.clustering.Cluster;
import server.data.Data;
import server.data.Example;

/**
 * Implementazione dell'interfaccia ClusterDistance che utilizza il metodo
 * average-link.
 * Nel metodo average-link la distanza tra due cluster è definita come la
 * sommatoria tra le distanze euclidee di ogni coppia di vettori Example
 * contenuti nei cluster divisa per il prodotto delle dimensioni dei cluster.
 */
public class AverageLinkDistance implements ClusterDistance {

    /**
     * Calcola e restituisce la distanza average-link tra i due Cluster.
     * 
     * @param c1 il primo cluster.
     * @param c2 il secondo cluster.
     * @param d  vettore Data contenente i vettori Example.
     * @return la distanza average-link tra i due Cluster.
     */
    public double distance(Cluster c1, Cluster c2, Data d) {
        double sum = 0;

        for (Integer index1 : c1) {
            Example e1 = d.getExample(index1);
            for (Integer index2 : c2) {
                double distance = e1.distance(d.getExample(index2));
                sum += distance;
            }
        }

        return sum / (c1.getSize() * c2.getSize());
    }
}
