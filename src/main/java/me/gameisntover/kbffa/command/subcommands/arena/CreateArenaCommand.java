package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.api.event.ArenaCreateEvent;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.regions.Cuboid;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.listeners.WandListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateArenaCommand extends KnockCommand {
    public CreateArenaCommand() {
        super("createarena");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Creates a new arena for the game";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        perform(KnockbackFFA.getInstance().getKnocker((Player) sender), args);
        return false;
    }


    @Override
    public String getSyntax() {
        return "/createarena <arenaname>";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "You must specify a name for the arena!");
            return;
        }
        if (WandListener.pos1m.get(p) == null && WandListener.pos2m.get(p) == null) {
            p.sendMessage(ChatColor.RED + "You must set the first and second positions of the arena!");
            return;
        } else if (WandListener.pos1m.get(p) != null && WandListener.pos2m.get(p) != null) {
            Location loc1 = WandListener.pos1m.get(p);
            Location loc2 = WandListener.pos2m.get(p);
            Arena arena = KnockbackFFA.getInstance().getArenaManager().create(args[0], loc1, loc2);
            List<String> blocks = new ArrayList<>();
            Cuboid region = new Cuboid(loc1, loc2);
            for (Block block : region.getBlocks()) blocks.add(block.getType().name());
            arena.getConfig().set("block-break", false);
            arena.getConfig().set("item-drop", true);
            arena.getConfig().set("world-border", false);
            arena.getConfig().set("block-break", false);
            arena.getConfig().set("item-drop", false);
            arena.getConfig().set("world-border", false);
            arena.getConfig().set("auto-reset", false);
            arena.getConfig().set("arena.pos1", loc1);
            arena.getConfig().set("arena.pos2", loc2);
            arena.getConfig().set("arena.spawn", p.getLocation());
            arena.getConfig().set("blocks", blocks);
            arena.save();
            if (KnockbackFFA.getInstance().getArenaManager().getFolder().list().length == 1)
                KnockbackFFA.getInstance().getArenaManager().setEnabledArena(args[0]);
            ArenaCreateEvent event = new ArenaCreateEvent(p, arena);
            Bukkit.getPluginManager().callEvent(event);
            p.sendMessage(ChatColor.GREEN + "Arena " + args[0] + " has been created!");
        }
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
