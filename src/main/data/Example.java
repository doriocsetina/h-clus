package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe che rappresenta il singolo esempio, un vettore costituito di un numero
 * variabile di valori reali.
 */
public class Example implements Iterable<Double> {
    private List<Double> example; // vettore di valori reali

    /**
     * Inizializza il campo example.
     */
    public Example() {
        example = new LinkedList<>();
    }

    /**
     * Restituisce un iteratore per la List di Example.
     * 
     * @return un Iterator per gli elementi Double nella List di Example.
     */
    public Iterator<Double> iterator() {
        return example.iterator();
    }

    /**
     * Aggiunge il valore v alla lista Example.
     * 
     * @param v valore da aggiungere ad Example.
     */
    public void add(Double v) {
        example.add(v);
    }

    /**
     * Restituisce il valore contenuto in Example all'indice dato in input.
     * 
     * @param index posizione della lista del valore richiesto.
     * @return valore memorizzato richiesto.
     */
    public Double get(int index) {
        return example.get(index);
    }

    /**
     * Restituisce la lunghezza della lista Example.
     * 
     * @return la lunghezza della lista Example.
     */
    public int getLength() {
        return example.size();
    }

    /**
     * Restituisce la distanza euclidea calcolata tra l'istanza di Example su cui è
     * applicato il metodo e l'istanza newE data in input
     * 
     * @param newE istanza di Example da cui si vuole calcolare la distanza
     * @return la distanza euclidea tra this.example e new.example
     */
    public double distance(Example newE) {
        double sum = 0;
        for (int i = 0; i < example.size(); i++) {
            sum += Math.pow(example.get(i) - newE.get(i), 2);
        }
        return sum;
    }

    /**
     * Restituisce la stringa rappresentante la lista example. 
     * 
     * @return la stringa che rappresenta il contenuto di example
     */
    public String toString() {
        String string = "[";
        Iterator<Double> iterator = example.iterator();
        while (iterator.hasNext()) {
            string += iterator.next();
            if (iterator.hasNext()) {
                string += ", ";
            }
        }
        string += "]";
        return string;
    }
}
