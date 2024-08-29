package clustering;

import java.io.Serializable;

import data.Data;

/**
 * Classe che rappresenta il dendrogramma, a cui a ognuno dei livelli viene
 * assegnato un insieme di cluster durante il processo di mining.
 * 
 */
public class Dendrogram implements Serializable {
    private ClusterSet tree[]; // modella il dendrogramma

    /**
     * Crea un vettore di dimensione depth con cui inizializza tree.
     * 
     * @param depth profondità del dendrogramma.
     */
    Dendrogram(int depth) {
        tree = new ClusterSet[depth];
    }

    /**
     * Memorizza c nella posizione level di tree.
     * 
     * @param c     ClusterSet da memorizzare.
     * @param level posizione all'interno di tree in cui memorizzare c.
     */
    void setClusterSet(ClusterSet c, int level) {
        tree[level] = c;
    }

    /**
     * Restituisce il ClusterSet al livello specificato.
     * 
     * @param level livello del ClusterSet da restituire.
     * @return il ClusterSet al livello specifcato.
     */
    ClusterSet getClusterSet(int level) {
        return tree[level];
    }

    /**
     * Restituisce la profondità del dendrogramma (ossia la dimensione di tree).
     * 
     * @return la dimensione del vettore tree.
     */
    int getDepth() {
        return tree.length;
    }

    /**
     * Fornisce una rappresentazione stringa dell'oggetto Dendrogram.
     *
     * @return una stringa che rappresenta l'oggetto Dendrogram.
     */
    public String toString() {
        String v = "";
        for (int i = 0; i < tree.length; i++)
            v += ("level" + i + ":\n" + tree[i] + "\n");
        return v;
    }

    /**
     * Fornisce una rappresentazione stringa dell'oggetto Dendrogram, dove i cluster
     * contengono esplicitamente i vettori Example.
     * 
     * @param data vettore Data dove sono contenuti i vettori Example.
     * @return na stringa che rappresenta l'oggetto Dendrogram.
     */
    String toString(Data data) {
        String v = "";
        for (int i = 0; i < tree.length; i++)
            v += ("level" + i + ":\n" + tree[i].toString(data) + "\n");
        return v;
    }
}
