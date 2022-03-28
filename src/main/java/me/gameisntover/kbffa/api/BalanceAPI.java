package me.gameisntover.kbffa.api;

import me.gameisntover.kbffa.customconfig.PlayerData;
import org.bukkit.entity.Player;

public class BalanceAPI {

    public void setBalance(Player p, float balance) {
        PlayerData.load(p);
        PlayerData.get().set("balance", balance);
        PlayerData.save();
    }

    public float getBalance(Player p) {
        PlayerData.load(p);
        return PlayerData.get().getInt("balance");
    }

    public void addBalance(Player p, float balance) {
        setBalance(p, getBalance(p) + balance);
    }

    public void removeBalance(Player p, int balance) {
        setBalance(p, getBalance(p) - balance);
    }
}
