package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clustering.exceptions.InvalidSizeException;
import database.DbAccess;
import database.TableData;
import database.exceptions.EmptySetException;
import database.exceptions.MissingNumberException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

/**
 * Classe che rappresenta il dataset su cui verrà operato il mining.
 * Contiene i vettori Example, il numero totale di istanze Example e il metodo
 * per calcolarne la distanza.
 */
public class Data {
	private List<Example> data = new ArrayList<>(); // rappresenta il dataset
	private int numberOfExamples; // rappresenta il numero di esempi nel dataset

	/**
	 * Costruttore che popola l'istanza di Data accedendo al database e leggendone
	 * le transazioni.
	 * 
	 * @param tableName nome del database da cui prendere le transazioni.
	 * @throws SQLException           se si verifica un errore durante l'esecuzione
	 *                                della query SQL.
	 * @throws EmptySetException      se la table specificata in input non contiene
	 *                                nessun elemento.
	 * @throws MissingNumberException se una transazione contiene caratteri non
	 *                                numerici.
	 */
	public Data(String tableName) throws SQLException, EmptySetException, MissingNumberException {
		// data
		DbAccess db = new DbAccess();
		TableData tableData = new TableData(db);

		this.data = tableData.getDistinctTransactions(tableName);

		// numberOfExamples
		numberOfExamples = data.size();
	}

	/**
	 * Restituisce il numero di vettori Example contenuti in Data.
	 * 
	 * @return numero di esempi memorizzati in data.
	 */
	public int getNumberOfExamples() {
		return this.numberOfExamples;
	}

	/**
	 * Restituisce l'esempio contenuto all'indice specificato.
	 *
	 * @param exampleIndex indice di un esempio memorizzato in data.
	 * @return l'esempio memorizzato richiesto.
	 */
	public Example getExample(int exampleIndex) {
		return data.get(exampleIndex);
	}

	/**
	 * Genera il checksum unico dei dati contenuti nell'istanza di data su cui viene
	 * chiamato il metodo. Il checksum rimane invariato se una diversa istanza di
	 * Data è popolata con gli stessi valori.
	 * 
	 * @return stringa esadecimale unica calcolata in base ai valori contenuti in
	 *         data.
	 */
	public String generateChecksum() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String input = this.toString();
			byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hash);
			StringBuilder hexString = new StringBuilder(number.toString(16));
			while (hexString.length() < 32) {
				hexString.insert(0, '0');
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Restituisce la matrice triangolare superiore delle distanze
	 * 
	 * @return matrice triangolare superiore delle distanze Euclidee calcolate tra
	 *         gli esempi memorizzati in data. Tale matrice va avvalorata usando il
	 *         metodo distance di Example.
	 * 
	 * @throws InvalidSizeException se due vettori Example contenuti all'interno di
	 *                              data non hanno lo stesso numero di elementi.
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
	 * Crea una stringa in cui memorizza gli esempi memorizzati nell’attributo data,
	 * opportunamente enumerati. Restituisce tale stringa.
	 * 
	 * @return stringa che modella lo stato dell'oggetto.
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
		try {
			Data trainingSet = new Data("exampleTab");
			System.out.println(trainingSet);
			double[][] distancematrix = trainingSet.distance();
			System.out.println("Distance matrix:\n");
			for (int i = 0; i < distancematrix.length; i++) {
				for (int j = 0; j < distancematrix.length; j++)
					System.out.print(distancematrix[i][j] + "\t");
				System.out.println("");
			}
		} catch (InvalidSizeException e) {
			System.err.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmptySetException e) {
			e.printStackTrace();
		} catch (MissingNumberException e) {
			e.printStackTrace();
		}

	}

}
