package me.gameisntover.kbffa.command.subcommands.worlds;

import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GotoWorldCommand extends KnockCommand {
    public GotoWorldCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getKnockDescription() {
        return ChatColor.translateAlternateColorCodes('&', "&5Teleports player to the specified world");
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/gotoworld <worldname>";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "You need to enter more details like what world you're going to travel..." + getSyntax());
            return;
        }
        World world = Bukkit.getWorld(args[0]);
        if (world != null) p.teleport(world.getSpawnLocation());
        else if (world == null) p.sendMessage(ChatColor.RED + "World does not exist!");
        else if (p.getWorld() == world) p.sendMessage(ChatColor.RED + "You are already in this world!");
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return Bukkit.getWorlds().stream().map(world -> world.getName()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
