package me.gameisntover.kbffa.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.util.CommonUtils;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathListener implements Listener {
    Map<Entity, Entity> killer = new HashMap<>();

    @EventHandler
    public void playerDamageCheck(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (!player.getType().equals(EntityType.PLAYER)) return;
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID)
            e.setDamage(KnockbackFFA.getInstance().getConfig().getInt("default-void-damage"));
        else e.setDamage(0);
    }

    @EventHandler
    public void checkDamagerFinalDamage(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        if (!player.getType().equals(EntityType.PLAYER)) return;
        if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) return;
        if (damager.getType().equals((EntityType.ARROW))) {
            Arrow arrow = (Arrow) damager;
            if (!(arrow.getShooter() instanceof Player)) return;
            Player shooter = (Player) arrow.getShooter();
            killer.put(player, shooter);
        } else if (damager instanceof Player || damager.hasMetadata("bot")) killer.put(player, damager);

    }


    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = killer.get(player);
        if (KnockbackFFA.getInstance().getArenaManager() == null) return;
        ReworkedKnocker knocker = ReworkedKnocker.get(player.getUniqueId());
        Arena arena = KnockbackFFA.getInstance().getArenaManager().getEnabledArena();
        killer.remove(player);
        ArenaManager.setInGame(player.getUniqueId(), false);
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                CommonUtils.giveLobbyItems(player);
                arena.teleportPlayer(player);
                cancel();
            }
        }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
        World world = player.getWorld();
        List<Entity> entList = world.getEntities();
        for (Entity current : entList) if ((current instanceof Item)) current.remove();
        knocker.addDeath();
        if (damager == null) {
            player.sendMessage(Message.DEATH_VOID.toString());
            e.setDeathMessage(Message.DEATH_VOID_GLOBAL.toString().replace("%player_name%", player.getName()));
        } else {
            if (damager != player && damager instanceof Player) {
                // TODO: wtf is this? knocker.loadCosmetic(knocker.getSelectedCosmetic());
                ReworkedKnocker damageKnocker = ReworkedKnocker.get(damager.getUniqueId());
                int prize = KnockbackFFA.getInstance().getConfig().getInt("kill-prize");
                damageKnocker.getPlayer().sendMessage(Message.PRIZE.toString().replace("%prize%", prize + "").replace("&", "§"));
                damageKnocker.addBalance(prize);
                damageKnocker.addKill();
                /* TODO: yes this bs should change
                damageKnocker.setKillStreak(damageKnocker.getKillStreak() + 1);
                if (damageKnocker.getKillStreak() > damageKnocker.getConfig().getInt("best-ks")) {
                    String msg = Message.KILLSTREAK_RECORD.toString().replace("%killstreak%", damageKnocker.getConfig().getInt("best-ks") + "");
                    damageKnocker.sendActionBar(msg);
                    damageKnocker.getConfig().set("best-ks", damageKnocker.getKillStreak());
                }
                damageKnocker.saveConfig();
                */
                e.setDeathMessage(PlaceholderAPI.setPlaceholders(e.getEntity(), Message.DEATH_KNOCKED_GOBAL.toString().replace("%killer%", damager.getName())));

            } else if (damager.hasMetadata("bot"))
                e.setDeathMessage(PlaceholderAPI.setPlaceholders(e.getEntity(), Message.DEATH_KNOCKED_GOBAL.toString().replace("%killer%", damager.getName())));
        }
    }
}
