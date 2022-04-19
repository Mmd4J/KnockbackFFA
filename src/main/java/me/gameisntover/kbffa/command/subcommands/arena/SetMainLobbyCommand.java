package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SetMainLobbyCommand extends KnockCommand {
    public SetMainLobbyCommand() {
        super("setmainlobby");
    }

    @Override
    public @NotNull String getKnockDescription() {
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
        KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("mainlobby.x", loc.getX());
        KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("mainlobby.y", loc.getY());
        KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("mainlobby.z", loc.getZ());
        String world = Objects.requireNonNull(loc.getWorld()).getName();
        KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("mainlobby.world", world);
        KnockbackFFA.getInstance().getZoneConfiguration().save();
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
