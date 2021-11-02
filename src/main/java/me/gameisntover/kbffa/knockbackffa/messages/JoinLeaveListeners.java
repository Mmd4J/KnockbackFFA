package me.gameisntover.kbffa.knockbackffa.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners  implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
                String joinText = KnockbackFFA.getInstance().getConfig().getString("joinmessage").replace("&", "§");
        joinText = PlaceholderAPI.setPlaceholders(e.getPlayer(), joinText);
        Player player = e.getPlayer();
        e.setJoinMessage(joinText);
        Double x = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.x");
        Double y = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.y");
        Double z = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.z");
        World world = KnockbackFFA.getInstance().getServer().getWorld(KnockbackFFA.getInstance().getConfig().getString("spawnpoint.world"));
        if (KnockbackFFA.getInstance().getConfig().getString("spawnpoint.x") == null ) {
            if(player.isOp()){
                player.sendMessage("§cSpawn is not set! Please set it in the config or /setspawn!");
            }else if (player.isOp() == false){
                player.sendMessage("§cAsk an admin to create the spawnpoint so i can get you there!");
            }
        }
        else {
            player.teleport(new org.bukkit.Location(world, x, y, z));

        }
    }
    @EventHandler
    public void playerLeave(PlayerQuitEvent e){
        String leaveText = KnockbackFFA.getInstance().getConfig().getString("leavemessage").replace("&", "§");
        leaveText = PlaceholderAPI.setPlaceholders(e.getPlayer(), leaveText);
        e.setQuitMessage(leaveText);
        Player player = e.getPlayer();
    }
}
