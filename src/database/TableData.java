package database;

public class TableData {
    private DbAccess db;

    TableData(DbAccess db) {
        db.initConnection();
    }
}
