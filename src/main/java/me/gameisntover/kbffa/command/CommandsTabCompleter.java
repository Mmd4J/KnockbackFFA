package me.gameisntover.kbffa.command;

import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("resetarena")) {
            if (args.length == 1) {
                List<String> arenaList = Arrays.asList(Arrays.stream(KnockbackFFA.getINSTANCE().getTempArenaManager().getDf().list()).map(s -> {
                    return s.replace(".yml", "");
                }).toArray(String[]::new));
                return arenaList;
            }
        }
        if (command.getName().equalsIgnoreCase("editarena")) {
            if (args.length == 1) {
                List<String> arenaList = Arrays.asList(Arrays.stream(KnockbackFFA.getINSTANCE().getTempArenaManager().getDf().list()).map(s -> {
                    return s.replace(".yml", "");
                }).toArray(String[]::new));
                return arenaList;
            }
        }
        if (command.getName().equalsIgnoreCase("gotoworld")) {
            if (args.length == 1) {
                List<String> worldList = Bukkit.getWorlds().stream().map(world -> world.getName()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                return worldList;
            }
        }
        return null;
    }
}
