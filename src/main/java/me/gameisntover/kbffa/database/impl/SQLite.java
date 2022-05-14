package me.gameisntover.kbffa.database.impl;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
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
                knocker.getOwnedKits().addAll(toList(result.getString("ownedKits")));
                knocker.getOwnedTrails().addAll(toList(result.getString("ownedTrails")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        knocker.save(false);
        return knocker;
    }

    @Override
    public void updateKnocker(ReworkedKnocker kncoker){
        String sql = "INSERT INTO kbffa(uuid,name,kills,deaths,balance,selectedKit,selectedTrail,ownedKits,ownedTrails) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            //update data here
            statement.executeUpdate();
        } catch (SQLException ignored) { /* should un-ignore later */ }  
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
                "uuid text PRIMARY KEY NOT NULL,\n" +
                "name VARCHAR(16) NOT NULL,\n" +
                "kills INT(20) NOT NULL DEFAULT 0,\n" +
                "deaths INT(20) NOT NULL DEFAULT 0,\n" +
                "maxkillstreak INT(20) NOT NULL DEFAULT 0,\n" +
                "balance INT(20) NOT NULL DEFAULT 0,\n" +
                "selectedCosmetic VARCHAR(30) NOT NULL,\n" +
                "selectedTrail VARCHAR(30) NOT NULL,\n" +
                "selectedKit VARCHAR(30) NOT NULL,\n" +
                "ownedKits MEDIUMTEXT NOT NULL, \n" +
                "ownedTrails MEDIUMTEXT NOT NULL, \n" +
                ");";
        try (Connection connection = createConnection()){
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String fromList(List<String> list){
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for (String string : list){
            builder.append(string);
            if(i != list.size()){
                builder.append(", ");
            }
            i++;
        }
        return builder.toString();
    }

    private List<String> toList(String string){
        return Arrays.asList(string.split(", "));
    }

}
