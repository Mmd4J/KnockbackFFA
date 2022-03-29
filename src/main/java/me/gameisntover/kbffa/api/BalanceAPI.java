package me.gameisntover.kbffa.api;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.entity.Player;

public class BalanceAPI {

    public void setBalance(Knocker knocker, float balance) {
        knocker.getConfig().set("balance", balance);
        knocker.save();
    }

    public float getBalance(Knocker knocker) {
        return knocker.getConfig().getInt("balance");
    }

    public void addBalance(Knocker knocker, float balance) {
        setBalance(knocker, getBalance(knocker) + balance);
    }

    public void removeBalance(Knocker knocker, int balance) {
        setBalance(knocker, getBalance(knocker) - balance);
    }
}
