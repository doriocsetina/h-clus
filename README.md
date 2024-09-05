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

avviamo il server:

`java -jar MultiServer.jar`

nel caso di test nella stessa macchina eseguiamo:

`java -jar MainTest.jar localhost 8080`

il server ha come porta impostata di default `8080`

eseguire quindi i passaggi richiesti dal client. 

### Running the GuiClient

eseguendo

`java -jar GuiClient.jar`

possiamo avviare il client dotato di GUI. Usando come indirizzo `localhost` e come porta `8080` accediamo alla schermata principale dell'applicazione che ci consente di eseguire tutte
le operazioni forniteci dal server.

Su macchine windows è anche possibile doppio-cliccare sull'archivio jar per avviare il client. (E' possibile anche per il server, tuttavia viene aperto come processo e non è più possible guardarne i log)

## CLI test example

avviamo il server:

`java -jar MultiServer.jar`

in un'altra istanza di terminale avviamo il client CLI, specificando indirizzo e porta:

`java -jar MainTest.jar localhost 8080`

proviamo con la prima tabella inserita dallo script SQL: `exampleTab`.

Ci sarà chiesto di scegliere tra:
1. caricare il dendrogramma da file;
2. apprendere il dendrogramma dal database;

scegliamo la seconda opzione.

Ci sarà dunque chiesto la profondità del dendrogramma, andiamo a mettere 5;

successivamente sarà chiesta la distanza, andando a mettere 2 otterremo:

```
level0:
cluster0:<[1.0, 2.0, 0.0]>
cluster1:<[0.0, 1.0, -1.0]>
cluster2:<[1.0, 3.0, 5.0]>
cluster3:<[1.0, 3.0, 4.0]>
cluster4:<[2.0, 2.0, 0.0]>

level1:
cluster0:<[0.0, 1.0, -1.0]>
cluster1:<[1.0, 3.0, 5.0]>
cluster2:<[1.0, 3.0, 4.0]>
cluster3:<[1.0, 2.0, 0.0]><[2.0, 2.0, 0.0]>

level2:
cluster0:<[0.0, 1.0, -1.0]>
cluster1:<[1.0, 2.0, 0.0]><[2.0, 2.0, 0.0]>
cluster2:<[1.0, 3.0, 5.0]><[1.0, 3.0, 4.0]>

level3:
cluster0:<[1.0, 3.0, 5.0]><[1.0, 3.0, 4.0]>
cluster1:<[1.0, 2.0, 0.0]><[0.0, 1.0, -1.0]><[2.0, 2.0, 0.0]>

level4:
cluster0:<[1.0, 2.0, 0.0]><[0.0, 1.0, -1.0]><[1.0, 3.0, 5.0]><[1.0, 3.0, 4.0]><[2.0, 2.0, 0.0]>
```

a questo punto ci sarà chiesto il nome, andiamo a porre: `example.dat`

il file sarà salvato nella cartella dove è stato eseguito il file!

### error handling

durante l'utilizzo della CLI, ci sono alcune misure di error handling. 
La scelta del nome della tabella è disponibile per tre tentativi, finiti i quali il server blocca la connessione.
Nel caso di calcolo del dendrogramma venga scelta una profondità maggiore degli esempi contenuti nel dataset, 
il risultato di ritorno conterrà il messaggio di errore prodotto dal server. 

Inoltre, se carichiamo un dendrogramma serializzato di cui non esiste il file, il client ritornerà un errore e chiuderà la connessione.
Allo stesso modo, se cercassimo di caricare il dendrogramma serializzato che però è stato calcolato su un altro database, il server tornerà un errore
e la connessione sarà chiusa. 

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