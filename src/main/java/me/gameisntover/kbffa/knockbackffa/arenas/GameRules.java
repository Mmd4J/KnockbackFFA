package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;

public class GameRules implements Listener {
    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        BoundingBox s1box =new BoundingBox(ArenaConfiguration.get().getDouble("arena1.safezone1.x"),ArenaConfiguration.get().getDouble("arena1.safezone1.y"),ArenaConfiguration.get().getDouble("arena1.safezone1.z"),ArenaConfiguration.get().getDouble("arena1.safezone2.x"),ArenaConfiguration.get().getDouble("arena1.safezone2.y"),ArenaConfiguration.get().getDouble("arena1.safezone2.z"));
        BoundingBox s2box = new BoundingBox(ArenaConfiguration.get().getDouble("arena2.safezone1.x"),ArenaConfiguration.get().getDouble("arena2.safezone1.y"),ArenaConfiguration.get().getDouble("arena2.safezone1.z"),ArenaConfiguration.get().getDouble("arena2.safezone2.x"),ArenaConfiguration.get().getDouble("arena2.safezone2.y"),ArenaConfiguration.get().getDouble("arena2.safezone2.z"));
        BoundingBox s3box = new BoundingBox(ArenaConfiguration.get().getDouble("arena3.safezone1.x"),ArenaConfiguration.get().getDouble("arena3.safezone1.y"),ArenaConfiguration.get().getDouble("arena3.safezone1.z"),ArenaConfiguration.get().getDouble("arena3.safezone2.x"),ArenaConfiguration.get().getDouble("arena3.safezone2.y"),ArenaConfiguration.get().getDouble("arena3.safezone2.z"));
        BoundingBox s4box = new BoundingBox(ArenaConfiguration.get().getDouble("arena4.safezone1.x"),ArenaConfiguration.get().getDouble("arena4.safezone1.y"),ArenaConfiguration.get().getDouble("arena4.safezone1.z"),ArenaConfiguration.get().getDouble("arena4.safezone2.x"),ArenaConfiguration.get().getDouble("arena4.safezone2.y"),ArenaConfiguration.get().getDouble("arena4.safezone2.z"));
        World s1world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena1.world"));
        World s2world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena2.world"));
        World s3world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena3.world"));
        World s4world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena4.world"));
        Player player = e.getPlayer();
        Location location = player.getLocation();
        if (s1box.contains(location.toVector())&& player.getWorld() == s1world ) {
            player.setInvulnerable(true);
        } else if(s2box.contains(location.toVector())&& player.getWorld() == s2world) {
            player.setInvulnerable(true);
        } else if (s3box.contains(location.toVector())&& player.getWorld() == s3world) {
            player.setInvulnerable(true);
        }else if(s4box.contains(location.toVector())&& player.getWorld() == s4world) {
            player.setInvulnerable(true);
        } else {
            player.setInvulnerable(false);
        }
    }
}
