package me.gameisntover.kbffa.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.customconfig.Knocker;
import me.gameisntover.kbffa.util.Message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathListener implements Listener {
    Map<Entity, Integer> killStreak = new HashMap<>();
    Map<Entity, Entity> killer = new HashMap<>();
    Arena Arena;
    @EventHandler
    public void playerDamageCheck(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (player.getType().equals(EntityType.PLAYER)) {
            if (KnockbackFFA.getINSTANCE().BungeeMode() || knocker.isInGame()) {
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setDamage(KnockbackFFA.getINSTANCE().getConfig().getInt("default-void-damage"));
                } else {
                    e.setDamage(0);
                }
            }
        }
    }

    @EventHandler
    public void checkDamagerFinalDamage(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        Knocker knockerPlayer = KnockbackFFA.getINSTANCE().getKnocker((Player) player);
        if (!player.getType().equals(EntityType.PLAYER)) return;
            if (!KnockbackFFA.getINSTANCE().BungeeMode() || !knockerPlayer.isInGame()) return;
                List<EntityDamageEvent.DamageCause> damageCauses = Arrays.asList(EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                        EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK, EntityDamageEvent.DamageCause.PROJECTILE);
                if (!damageCauses.contains(e.getCause())) return;
                    if (damager.getType().equals((EntityType.ARROW))) {
                        Arrow arrow = (Arrow) damager;
                        if (!(arrow.getShooter() instanceof Player)) return;
                            Player shooter = (Player) arrow.getShooter();
                            killer.put(player, shooter);
                    } else if (damager instanceof Player) {
                        killer.put(player, damager);
                    }
            }

    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = killer.get(player);
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        killer.remove(player);
        knocker.setInArena(false);
        if (!KnockbackFFA.getINSTANCE().BungeeMode() || !knocker.isInGame()) return;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.spigot().respawn();
                    KnockbackFFAKit kitManager = new KnockbackFFAKit();
                    kitManager.lobbyItems(player);
                    Arena.teleportPlayer(player);
                    cancel();
                }
            }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, 1);
            World world = player.getWorld();
            List<Entity> entList = world.getEntities();
            for (Entity current : entList) {
                if (!(current instanceof Item)) return;
                    current.remove();
            }
            killStreak.put(player, 0);
            knocker.getConfig().set("deaths", knocker.getConfig().getInt("deaths") + 1);
            knocker.saveConfig();
            if (damager != null && damager != player) {
                knocker.loadCosmetic(knocker.selectedCosmetic());

                Knocker damageKnocker = KnockbackFFA.getINSTANCE().getKnocker((Player) damager);
                float prize = KnockbackFFA.getINSTANCE().getConfig().getInt("killprize");
                damager.sendMessage(Message.PRIZE.toString().replace("%prize%", prize + "").replace("&", "§"));
                KnockbackFFA.getINSTANCE().getBalanceAPI().addBalance(KnockbackFFA.getINSTANCE().getKnocker((Player) damager), prize);
                damageKnocker.getConfig().set("kills", damageKnocker.getConfig().getInt("kills") + 1);
                killStreak.merge(damager, 1, Integer::sum);
                if (killStreak.get(damager) > damageKnocker.getConfig().getInt("best-ks")) {
                    Player damagerP = (Player) damager;
                    String msg = Message.KILLSTREAK_RECORD.toString().replace("%killstreak%", damageKnocker.getConfig().getInt("best-ks") + "");
                    damagerP.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
                    damageKnocker.getConfig().set("best-ks", killStreak.get(damager));
                }
                damageKnocker.saveConfig();
                String deathText = Message.DEATH_KNOCKED_GOBAL.toString().replace("%killer%", damager.getName());
                deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
                e.setDeathMessage(deathText);
            } else {
                player.sendMessage(Message.DEATH_VOID.toString());
                e.setDeathMessage(Message.DEATH_VOID_GLOBAL.toString().replace("%player_name%", player.getName()));
            }
        }
}
