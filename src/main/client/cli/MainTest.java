package client.cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import client.util.Keyboard;

public class MainTest {

	/**
	 * @param args
	 */
	private ObjectOutputStream out;
	private ObjectInputStream in; // stream con richieste del client

	public MainTest(String ip, int port) throws IOException {
		InetAddress addr = InetAddress.getByName(ip); // ip
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, port); // Port
		System.out.println(socket);

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		; // stream con richieste del client
	}

	private int menu() {
		int answer;
		System.out.println("Scegli una opzione");
		do {
			System.out.println("(1) Carica Dendrogramma da File");
			System.out.println("(2) Apprendi Dendrogramma da Database");
			System.out.print("Risposta:");
			answer = Keyboard.readInt();
		} while (answer <= 0 || answer > 2);
		return answer;

	}

	private void loadDataOnServer() throws IOException, ClassNotFoundException {
		boolean flag = false;
		do {
			System.out.println("Nome tabella:");
			String tableName = Keyboard.readString();
			out.writeObject(0);
			out.writeObject(tableName);
			String risposta = (String) (in.readObject());
			if (risposta.equals("OK"))
				flag = true;
			else
				System.out.println(risposta);

		} while (flag == false);
	}

	private void loadDedrogramFromFileOnServer() throws IOException, ClassNotFoundException {
		System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
		String fileName = Keyboard.readString();

		out.writeObject(1);
		out.writeObject(fileName);
		String risposta = (String) (in.readObject());
		if (risposta.equals("OK"))
			System.out.println(in.readObject()); // stampo il dendrogramma che il server mi sta inviando
		else
			System.out.println(risposta); // stampo il messaggio di errore
	}

	private void mineDedrogramOnServer() throws IOException, ClassNotFoundException {

		out.writeObject(2);
		System.out.println("Introdurre la profondità del dendrogramma");
		int depth = Keyboard.readInt();
		out.writeObject(depth);
		int dType = -1;
		do {
			System.out.println("Distanza: single-link (1), average-link (2):");
			dType = Keyboard.readInt();
		} while (dType <= 0 || dType > 2);
		out.writeObject(dType);

		String risposta = (String) (in.readObject());
		if (risposta.equals("OK")) {
			System.out.println(in.readObject()); // stampo il dendrogramma che il server mi sta inviando
			System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
			String fileName = Keyboard.readString();
			out.writeObject(fileName);
		} else
			System.out.println(risposta); // stampo il messaggio di errore
	}

	public static void main(String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		MainTest main = null;
		try {
			main = new MainTest(ip, port);
			main.out.writeObject("cli");
			main.loadDataOnServer();
			int scelta = main.menu();
			if (scelta == 1)
				main.loadDedrogramFromFileOnServer();
			else
				main.mineDedrogramOnServer();

			main.out.writeObject(-1);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e);
			return;
		}
	}

}
