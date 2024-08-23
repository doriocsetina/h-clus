/**
 * implementazione dell'interfaccia ClusterDistance che utilizza il metodo
 * average-link.
 * nel metodo average-link la distanza tra due cluster è definita come la
 * sommatoria tra le distanze euclidee di ogni coppia di vettori Example
 * contenuti nei cluster divisa per il prodotto delle dimensioni dei cluster.
 */
public class AverageLinkDistance implements ClusterDistance {

    /**
     * calcola e restituisce la distanza average-link tra i due Cluster.
     * 
     * @param c1 il primo cluster.
     * @param c2 il secondo cluster.
     * @param d vettore Data contenente i vettori Example. 
     * @return la distanza average-link tra i due Cluster.
     */
    public double distance(Cluster c1, Cluster c2, Data d) {
        double sum = 0;

        for (int i = 0; i < c1.getSize(); i++) {
            Example e1 = d.getExample(c1.getElement(i));
            for (int j = 0; j < c2.getSize(); j++) {
                double distance = e1.distance(d.getExample(c2.getElement(j)));
                sum += distance;
            }
        }
        return sum / (c1.getSize() * c2.getSize());
    }
}
