package me.gameisntover.kbffa.api;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This is a reworked version of the kbffa custom player since it has to be cleaned up for any SQL database
 * This class won't be used yet.
 */

@Data
public class ReworkedKnocker {

    private static final HashMap<UUID, ReworkedKnocker> KNOCKERS = new HashMap<>();

    private final UUID playerID;
    private final List<String> ownedCosmetics = new ArrayList<>();
    private final List<String> ownedKits = new ArrayList<>();
    private final List<String> ownedTrails = new ArrayList<>();
    private String selectedCosmetic, selectedKit, selectedTrail;
    private int kills, deaths, balance;
    private boolean scoreboardEnabled = true;

    /**
     * @return the loaded knocker player
     */
    public static ReworkedKnocker get(UUID playerID) {
        return KNOCKERS.get(playerID);
    }

    /**
     * unloads the player from the KNOCKERS map
     */
    public static void unload(UUID playerID) {
        KNOCKERS.remove(playerID);
    }

    /**
     * Adds a kill to the player kills
     */
    public void addKill(){
        this.kills += 1;
    }

    /**
     * Adds a death to the player deaths
     */
    public void addDeath(){
        this.deaths += 1;
    }

    /**
     * Adds balance to the player's
     * @param balance is the added amount of balance
     */
    public void addBalance(int balance){
        this.balance += balance;
    }

    /**
     * @param balance is the removed amount of balance
     * @return true if he has enough balance false if not
     */
    public boolean removeBalance(int balance){
        if(this.balance >= balance){
            this.balance -= balance;
            return true;
        }
        return false;
    }

    /**
     * @param override is whether should it override the player in the map
     */
    public void save(boolean override) {
        if(KNOCKERS.containsKey(this.playerID) && !override) {
            return;
        }
        KNOCKERS.put(this.playerID, this);
    }

    /**
     * @return The bukkit player through the UUID
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerID);
    }

}
