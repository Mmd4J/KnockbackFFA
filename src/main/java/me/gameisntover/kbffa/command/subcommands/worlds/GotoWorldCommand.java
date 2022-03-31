package me.gameisntover.kbffa.command.subcommands.worlds;

import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GotoWorldCommand extends SubCommand {
    @Override
    public String getName() {
        return "gotoworld";
    }

    @Override
    public String getDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5Teleports player to the specified world");
    }

    @Override
    public String getSyntax() {
        return "/gotoworld <worldname>";
    }

    @Override
    public String getPermission() {
        return "knockbackffa.command.gotoworld";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length == 0){
            p.sendMessage(ChatColor.RED + "You need to enter more details like what world you're going to travel..." + getSyntax());
        return;
        }
        World world = Bukkit.getWorld(args[0]);
        if (world != null) p.teleport(world.getSpawnLocation());
        else if (world == null) p.sendMessage(ChatColor.RED + "World does not exist!");
        else if (p.getWorld() == world) p.sendMessage(ChatColor.RED + "You are already in this world!");
    }

    @Override
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return Bukkit.getWorlds().stream().map(world -> world.getName()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
