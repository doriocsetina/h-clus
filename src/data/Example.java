package data;

public class Example {
    private Double[] example; // vettore di valori reali

    /**
     * inizializza example come vettore di dimensione length
     * 
     * @param lenght dimensione dell'esempio
     */
    public Example(int length) {
        this.example = new Double[length];
    }

    /**
     * modifica example inserendo v in posizione index
     * 
     * @param index posizione del valore
     * @param v     valore
     */
    public void set(int index, Double v) {
        this.example[index] = v;
    }

    /**
     * restituisce example[index]
     * 
     * @param index posizione di example
     * @return valore memorizzato in example[index]
     */
    public Double get(int index) {
        return this.example[index];
    }

    /**
     * restituisce la distanza calcolata
     * 
     * @param newE istanza di Example
     * @return la distanza euclidea tra this.example e new.example
     */
    public double distance(Example newE) {
        double sum = 0;
        for (int i = 0; i < this.example.length; i++) {
            sum += Math.pow(this.example[i] - newE.get(i), 2);
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
        for (int i = 0; i < example.length; i++) {
            string += example[i];
            if (i < example.length - 1) {
                string += ", ";
            }
        }
        string += "]";
        return string;
    }
}
