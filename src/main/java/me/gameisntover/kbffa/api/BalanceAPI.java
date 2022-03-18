package me.gameisntover.kbffa.api;

import me.gameisntover.kbffa.customconfigs.PlayerData;
import org.bukkit.entity.Player;

public class BalanceAPI {

    public static void setBalance(Player p, float balance) {
        PlayerData.load(p);
        PlayerData.get().set("balance", balance);
        PlayerData.save();
    }

    public static float getBalance(Player p) {
        PlayerData.load(p);
        return PlayerData.get().getInt("balance");
    }

    public static void addBalance(Player p, float balance) {
        setBalance(p,getBalance(p) + balance);
    }

    public static void removeBalance(Player p, int balance) {
        setBalance(p,getBalance(p) - balance);
    }

}
