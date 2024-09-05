# h-clus
a java exam

## installation

cloniamo la repository: 

`git clone https://github.com/doriocsetina/h-clus.git`

entriamo nella repository

`cd h-clus/`

### dependencies

il progetto si basa su quattro librerie:

1. [driver jdbc](https://dev.mysql.com/downloads/connector/j/), per gestire le operazioni di connessione al database SQL.
2. [jackson-core](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/), per gestire le operazioni core del formato JSON.
3. [jackson-databind](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/), per gestire le operazioni di serializzazione degli oggetti JSON.
4. [jackson-annotations](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/), dipendenza delle due librerie precedenti.

scaricare i rispettivi jar delle librerie e posizionarli all'interno della cartella `lib/`
durante lo sviluppo dell'applicazione è stata utilizzata la versione 2.17.2 della libreria jackson. 

### install 

eseguiamo lo script `./install.sh` per sistemi linux;

eseguiamo lo script `.\install.ps1` per sistemi windows.

verranno creati i file jar del server `MultiServer.jar` e dei due client: `MainTest.jar`, il client CLI e `GuiClient.jar` il client GUI dotato di interfaccia grafica. 


## Running the application

avviamo il server tramite il file `esegui_sever.bat`

avviamo dunque il client GUI tramite il file `esegui_client.bat`

Usando come indirizzo `localhost` e come porta `8080` accediamo alla schermata principale dell'applicazione che ci consente di eseguire tutte
le operazioni forniteci dal server.

Su macchine windows è anche possibile doppio-cliccare sull'archivio jar per avviare il client. (E' possibile anche per il server, tuttavia viene aperto come processo e non è più possible guardarne i log)

## GUI test example

La gui si apre con una schermata di login. Andiamo a popolare il login con `localhost` come indirizzo e `8080` come porta.

Verremo accolti dalla finestra principale del programma! è composta di quattro componenti importanti: 

1. in alto possiamo trovare il menu a tendina delle tabelle. Sono tutte le tabelle disponibli presenti all'interno del database. Tutte le operazioni avverranno in funzione della tabella selezionata.
2. il primo pannello, in alto a sinistra, composto delle opzioni di caricamento dendrogramma
3. il secondo pannello, in alto a destra, composto dalle opzioni di mining e di calcolo del dendrogramma
4. il terzo pannello, in basso, composto del text output del server e dei messaggi di errore/info presenti in basso. 

### calcolare un dendrogramma

è possible specificare la profondità tramite un input numerico e la distanza tramite un menù a tendina che mostra le opzioni disponibli.
il text field dove inserire la profondità non accetta input non numerici, che farebbero scattare i messaggi di errore interno alla GUI.

Se si dovesse anche qui usare un numero di profondità più alto del numero di esempi disponibli nel database il server tornerebbe un errore
che sarebbe mostrato a schermo nell'output del server.

è possibile specificare se si vuole salvare il file o meno. Se si dovesse scegliere di salvare è obbligatorio inserire un nome. Se si decide di
non serializzare il file il campo non sarà disponibile. Al corretto salvataggio di un dendrogramma saremo avvisati da una info all'interno della GUI
e dal dendrogramma stampato a schermo. 

### caricare un dendrogramma

è possibile caricare un dendrogramma specificandone il nome. Nel caso non esista un file serializzato con questo nome, il server invierà a video un errore.