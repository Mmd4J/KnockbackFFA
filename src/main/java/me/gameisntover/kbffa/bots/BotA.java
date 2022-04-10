package me.gameisntover.kbffa.bots;

import me.gameisntover.kbffa.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BotA extends Bot {
    public BotA(String name, Location location) {
        super(name, location);
    }

    @Override
    public void start() {
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(Message.CHATFORMAT.toString().replace("%player%", super.getName()).replace("%message%", "Hey everyone im here to kill you all!!"));
    }

    @Override
    public void update() {
        super.zombie.setTarget(null);
    }
}
