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
        createTables();
    }

    @Override
    public ReworkedKnocker getKnocker(UUID uuid) {
        String sql = "SELECT * FROM kbffa WHERE uuid=" + uuid;
        ReworkedKnocker knocker = new ReworkedKnocker(uuid);
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if(result.first()){
                knocker.setKills(result.getInt("kills"));
                knocker.setDeaths(result.getInt("deaths"));
                knocker.setBalance(result.getInt("balance"));
                knocker.setSelectedCosmetic(result.getString("selectedCosmetic"));
                knocker.setSelectedTrail(result.getString("selectedTrail"));
                knocker.setSelectedKit(result.getString("selectedKit"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return knocker;
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

    private void createTables(){
        String sql = "CREATE TABLE IF NOT EXIST kbffa (\n" +
                "uuid text PRIMARY KEY,\n" +
                "name text NOT NULL,\n" +
                "kills integer NOT NULL DEFAULT 0,\n" +
                "deaths integer NOT NULL DEFAULT 0,\n" +
                "maxkillstreak integer NOT NULL DEFAULT 0,\n" +
                "balance integer NOT NULL DEFAULT 0,\n" +
                "selectedCosmetic text NOT NULL,\n" +
                "selectedTrail text NOT NULL,\n" +
                "selectedKit text NOT NULL,\n" +
                ");";

        try (Connection connection = createConnection()){
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
