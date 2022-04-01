package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.ArenaConfiguration;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SetMainLobbyCommand extends SubCommand {
    private String name;
    public SetMainLobbyCommand(String name) {
        super(name);
        this.name = name;
    }
    @Override
    public @NotNull String getSubName() {
        return name;
    }

    @Override
    public @NotNull String getSubDescription() {
        return "&5Sets mainlobby location to the player's location";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/setmainlobby";
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
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
