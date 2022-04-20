package me.gameisntover.kbffa.api;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is a reworked version of {@link Knocker} since it has to be cleaned up for any SQL database
 * This class won't be used yet.
 */

@Data
public class ReworkedKnocker {

    private final UUID playerID;
    private final List<String> ownedKits = new ArrayList<>();
    private final List<String> ownedTrails = new ArrayList<>();
    private String selectedKit, selectedCosmetic, selectedTrail;
    private int kills, deaths, balance;
    private boolean scoreboardEnabled = true;

    /**
     * Adds a kill to the player kills
     */
    public void addKill(){
        kills += 1;
    }

    /**
     * Adds a death to the player deaths
     */
    public void addDeath(){
        deaths += 1;
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
     * @return The bukkit player through the UUID
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(playerID);
    }

}
