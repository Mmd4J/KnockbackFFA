package me.gameisntover.kbffa.database;

import me.gameisntover.kbffa.database.impl.SQLite;

public class DatabaseManager {

    public DatabaseManager(String databaseType){
        DatabaseType type = DatabaseType.valueOf(databaseType);
        Database database;
        switch (type){
            case SQLITE:
                database = new SQLite();
                break;
            case MYSQL:
                database = null;
                break;
        }

    }

}
