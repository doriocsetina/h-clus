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
     * inizializza example come vettore di dimensione length.
     * 
     * @param lenght dimensione dell'esempio.
     */
    public Example() {
        example = new LinkedList<>();
    }

    /**
     * restituisce un iteratore per la LinkedList di Example.
     * 
     * @return un Iterator per gli elementi Double nella LinkedList di Example.
     */
    public Iterator<Double> iterator() {
        return example.iterator();
    }

    /**
     * modifica example inserendo v in posizione index.
     * 
     * @param index posizione del valore.
     * @param v     valore.
     */
    public void add(Double v) {
        example.add(v);
    }

    /**
     * restituisce example[index]
     * 
     * @param index posizione di example
     * @return valore memorizzato in example[index]
     */
    public Double get(int index) {
        return example.get(index);
    }

    public int getLength() {
        return example.size();
    }

    /**
     * restituisce la distanza calcolata
     * 
     * @param newE istanza di Example
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
     * restituisce la stringa
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
