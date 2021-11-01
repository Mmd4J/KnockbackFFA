package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


import java.util.HashMap;
import java.util.Map;

public class deathlistener implements Listener {
    Map<Entity,Integer > killStreak = new HashMap<>();
    Map<Entity, Entity> damagers = new HashMap<>();
    @EventHandler
    public void damaged(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player && player instanceof Player) {

            damagers.put(player, damager);
        }
    }

    @EventHandler
    public void voidDamage(EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT.VOID) {
                p.setHealth(0.5);
            }

        }

    }

    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();

        Entity damager = damagers.get(player);
        if (damager == null) {
        player.sendMessage(ChatColor.AQUA + "You died by falling into the void");
        } else {
                if(killStreak.get(damager == null) == null) {
                    killStreak.put(damager, 0);
                }
            killStreak.put(damager, killStreak.get(damager).intValue() +1);
            damagers.remove(player);
            String deathText = KnockbackFFA.getInstance().getConfig().getString("deathmessage").replace("&", "§");
            deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
            e.setDeathMessage(deathText);

            e.setDeathMessage(KnockbackFFA.getInstance().getConfig().getString("deathmessage").replace("%player%", player.getDisplayName()).replace("&", "§").replace("%killer%", damager.getName()).replace("%kills%",killStreak.get(damager).toString()));
            damager.sendMessage(ChatColor.GREEN + "You killed " + ChatColor.BOLD + player.getDisplayName() + " " );
            Double x = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.x");
            Double y = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.y");
            Double z = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.z");
            World world = KnockbackFFA.getInstance().getServer().getWorld(KnockbackFFA.getInstance().getConfig().getString("spawnpoint.world"));
        }
        player.spigot().respawn();
    }
}
