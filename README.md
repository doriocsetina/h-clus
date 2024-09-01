# h-clus
a java exam

## installation (linux)

cloniamo la repository: 

`git clone https://github.com/doriocsetina/h-clus.git`

entriamo nella repository

`cd h-clus/`

### dependencies

il progetto si basa su tre librerie:

1. [driver jdbc](https://dev.mysql.com/downloads/connector/j/), per gestire le operazioni di connessione al database SQL.
2. [jackson-core](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/), per gestire le operazioni core del formato JSON.
3. [jackson-databind](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/), per gestire le operazioni di serializzazione degli oggetti JSON.
4. [jackson-annotations](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/), dipendenza delle due librerie precedenti.

scaricare i rispettivi jar delle librerie e posizionarli all'interno della cartella `lib/`

### install

eseguiamo lo script `./install.sh`

verranno creati i file jar del server `MultiServer.jar` e del client `MainTest.jar`

## Running the application

avviamo il server:

`java -jar MultiServer.jar`

nel caso di test nella stessa macchina eseguiamo:

`java -jar MainTest.jar localhost 8080`

il server ha come porta impostata di default `8080`

eseguire quindi i passaggi richiesti dal client. 


## building server from source

compiliamo tutte le classi del progetto all'interno della cartella `target/`, usando find per trovare tutti i file .java all'interno dei packages:

`javac -cp lib/* -d target/ $(find src -name "*.java")`

estraiamo le classi dal driver jdbc in formato jar contenuto all'interno di `lib/`

`unzip -d target/ lib/name-of-jdbc-driver.jar`

creiamo il file .jar:

`jar cfe MultiServer.jar server.Multiserver -c target/ .`

ora è possibile eseguire il server tramite il comando:

`java -jar MultiServer.jar`

e rimuovere la cartella target:

`rm -rf target/`


## building client from source

compiliamo il codice del client:

`javac -d target/ $(find src -name "*.java")`

creiamo il jar 

`jar cfe MainTest MainTest -C target/ .`

ora è possibile eseguire il client tramite il comando:

`java -jar Maintest.jar ADDRESS PORT`


