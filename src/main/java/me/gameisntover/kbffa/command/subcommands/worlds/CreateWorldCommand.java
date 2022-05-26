package me.gameisntover.kbffa.command.subcommands.worlds;

import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.VoidChunkGenerator;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateWorldCommand extends KnockCommand {

    public CreateWorldCommand() {
        super("createworld");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Generates / load a custom world which is useful for creating arenas!";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/createworld <worldname>";
    }


    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        if (args.length == 0) {
            knocker.getPlayer().sendMessage(ChatColor.RED + "You need to select a name..." + getSyntax());
            return;
        }
        WorldCreator wc = new WorldCreator(args[0]);
        wc.generateStructures(false);
        wc.generator(new VoidChunkGenerator());
        wc.createWorld();
        World world = Bukkit.getWorld(args[0]);
        if (world != null) world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        assert world != null;
        Block stone = world.getSpawnLocation().getBlock();
        if (stone.getType() == Material.AIR) stone.setType(Material.STONE);
        knocker.getPlayer().sendMessage(ChatColor.GREEN + "World " + args[0] + " has been loaded");
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
