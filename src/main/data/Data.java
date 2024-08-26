package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clustering.exceptions.InvalidSizeException;
import database.DbAccess;
import database.TableData;
import database.exceptions.EmptySetException;
import database.exceptions.MissingNumberException;

/**
 * Classe che rappresenta il dataset su cui verrà operato il mining.
 * Contiene i vettori Example, il numero totale di istanze Example e il metodo
 * per calcolarne la distanza.
 */
public class Data {
	private List<Example> data = new ArrayList<>(); // rappresenta il dataset
	private int numberOfExamples; // rappresenta il numero di esempi nel dataset

	/**
	 * Costruttore con vettori Exampes hard-coded.
	 */
	public Data(String tableName) {

		// data
		DbAccess db = new DbAccess();
		TableData tableData = new TableData(db);

		try {
			this.data = tableData.getDistinctTransactions(tableName);
		} catch (SQLException | EmptySetException | MissingNumberException e) {
			e.printStackTrace();
		}

		// numberOfExamples
		numberOfExamples = data.size();

	}

	/**
	 * restituisce numberOfExamples
	 * 
	 * @return numero di esempi memorizzati in data
	 */
	public int getNumberOfExamples() {
		return this.numberOfExamples;
	}

	/**
	 * restituisce data[exampleIndex]
	 *
	 * @param exampleIndex indice di un esempio memorizzato in data
	 * @return l'esempio memorizzato in data[exampleIndex]
	 */
	public Example getExample(int exampleIndex) {
		return data.get(exampleIndex);
	}

	/**
	 * restituisce la matrice triangolare superiore delle distanze
	 * 
	 * @return matrice triangolare superiore delle distanze Euclidee calcolate tra
	 *         gli esempi memorizzati in data. Tale matrice va avvalorata usando il
	 *         metodo distance di Example
	 */
	public double[][] distance() throws InvalidSizeException {
		double[][] distance = new double[this.numberOfExamples][this.numberOfExamples];
		for (int i = 0; i < numberOfExamples; i++) {
			for (int j = 0; j < numberOfExamples; j++) {
				if (this.getExample(i).getLength() != this.getExample(j).getLength())
					throw new InvalidSizeException();
				if (i < j) {
					distance[i][j] = this.getExample(i).distance(this.getExample(j));
				}
			}
		}
		return distance;
	}

	/**
	 * crea una stringa in cui memorizza gli esempi memorizzati nell’attributo data,
	 * opportunamente enumerati. Restituisce tale stringa
	 * 
	 * @return stringa che modela lo stato dell'oggetto
	 */
	public String toString() {
		String string = "";
		for (Example e : data) {
			int i = 0;
			string += i++ + ":" + e + "\n";
		}

		return string;
	}

	public static void main(String args[]) {
		Data trainingSet = new Data("exampleTab");
		System.out.println(trainingSet);
		try {
			double[][] distancematrix = trainingSet.distance();
			System.out.println("Distance matrix:\n");
			for (int i = 0; i < distancematrix.length; i++) {
				for (int j = 0; j < distancematrix.length; j++)
					System.out.print(distancematrix[i][j] + "\t");
				System.out.println("");
			}
		} catch (InvalidSizeException e) {
			System.err.println(e.getMessage());
		}

	}

}
