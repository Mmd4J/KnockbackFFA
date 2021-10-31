package me.gameisntover.knockbackffa;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathListener implements Listener {
             Map<Entity,Entity> damagers = new HashMap<>();
    Map<String,Integer> killStreak = new HashMap<String,Integer>();

    @EventHandler
    public void damaged(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player && player instanceof Player  ) {

                damagers.put(player, damager);
            }
        }

    @EventHandler
    public void voidDamage(EntityDamageEvent e){
            if(e.getEntity() instanceof Player) {
                if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT.VOID){
                    Player p = (Player) e.getEntity();
                    p.setHealth(0.5);

                    p.sendMessage("okb");

                }
            }
        }

        @EventHandler
        public void playerDeathByVoid(PlayerDeathEvent e) {
            Player player = e.getEntity();
            Entity damager = damagers.get(player);
            String damagername = damager.getName();
           damagers.remove(player);
                e.setDeathMessage(ChatColor.GRAY + "[ " + ChatColor.RED + player.getDisplayName() + ChatColor.GRAY + " ]" + ChatColor.AQUA + " was killed by " + ChatColor.GRAY + "[ " + ChatColor.AQUA + damager.getName() + ChatColor.GRAY + " ]");
            killStreak.put(damagername, killStreak.containsValue(damagername) ? killStreak.get(damagername) + 1 : 1);

            killStreak.get(damagername);
            damager.sendMessage(ChatColor.GREEN+ "You killed " +ChatColor.BOLD   + player.getDisplayName() +" "+ killStreak.get(damagername) + " kills!" );

            if (killStreak.get(damagername)==5 ){
                damager.sendMessage("Nice job man you just reached to 5 kill streaks");

            } if (damager.isDead()){
                killStreak.put(damagername,0);
            }
        }
    }
