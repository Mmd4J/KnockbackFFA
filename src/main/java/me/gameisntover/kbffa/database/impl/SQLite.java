package me.gameisntover.kbffa.database.impl;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SQLite implements Database {

    private final String url;

    public SQLite(){
        File file = new File(KnockbackFFA.getInstance().getDataFolder() + File.separator + "Database.db");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        url = "jdbc:sqlite:" + file.getPath();
        //TODO: create kbffa table
    }

    @Override
    public ReworkedKnocker getKnocker(UUID uuid) {
        String sql = "SELECT * FROM kbffa WHERE uuid=" + uuid;
        try {
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            if(set.first()){
                //TODO: get the knocker
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Connection createConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
