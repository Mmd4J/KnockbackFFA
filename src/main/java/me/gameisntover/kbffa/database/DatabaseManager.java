package me.gameisntover.kbffa.database;

import me.gameisntover.kbffa.database.impl.SQLite;

public class DatabaseManager {

    private final Database database;

    public DatabaseManager(String databaseType){
        DatabaseType type = DatabaseType.valueOf(databaseType);
        switch (type){
            case SQLITE:
                database = new SQLite();
                break;
            case MYSQL:
                database = null;
                break;
            default: database = new SQLite();
        }

    }

}
