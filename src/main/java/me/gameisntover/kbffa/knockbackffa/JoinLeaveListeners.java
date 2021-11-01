package me.gameisntover.kbffa.knockbackffa;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public class JoinLeaveListeners  implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();e.setJoinMessage(KnockbackFFA.getInstance().getConfig().getString("joinmessage").replace("%player%", player.getDisplayName()).replace("&", "§"));
        Double x = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.x");
        Double y = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.y");
        Double z = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.z");
        World world = KnockbackFFA.getInstance().getServer().getWorld(KnockbackFFA.getInstance().getConfig().getString("spawnpoint.world"));
        player.teleport(new org.bukkit.Location(world, x, y, z));
        if (KnockbackFFA.getInstance().getConfig().getString("spawn") == null) {
        } else {


        }
        player.getWorld().setSpawnLocation(x.intValue(), y.intValue(), z.intValue());
    }
    @EventHandler
    public void playerLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(KnockbackFFA.getInstance().getConfig().getString("leavemessage").replace("%player%" , player.getDisplayName()).replace("&", "§"));
    }
}
