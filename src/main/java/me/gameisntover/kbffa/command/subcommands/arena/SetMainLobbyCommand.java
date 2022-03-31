package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.ArenaConfiguration;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class SetMainLobbyCommand extends SubCommand {
    @Override
    public String getName() {
        return "setmainlobby";
    }

    @Override
    public String getDescription() {
        return "&5Sets mainlobby location to the player's location";
    }

    @Override
    public String getSyntax() {
        return "/setmainlobby";
    }

    @Override
    public String getPermission() {
        return "knockbackffa.command.setmainlobby";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        p.sendMessage(ChatColor.GREEN + "Main lobby spawn has been set");
        Location loc = p.getLocation();
        ArenaConfiguration.get().set("mainlobby.x", loc.getX());
        ArenaConfiguration.get().set("mainlobby.y", loc.getY());
        ArenaConfiguration.get().set("mainlobby.z", loc.getZ());
        String world = Objects.requireNonNull(loc.getWorld()).getName();
        ArenaConfiguration.get().set("mainlobby.world", world);
        ArenaConfiguration.save();
    }

    @Override
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return null;
    }
}
