package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import org.bukkit.entity.Player;

public class balanceAPI {

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
