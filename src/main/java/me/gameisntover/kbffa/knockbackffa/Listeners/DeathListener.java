package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.API.balanceAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathListener implements Listener
{
    Map<Entity, Integer> killStreak = new HashMap<>();
    Map<Entity, Entity> killer = new HashMap<>();

    @EventHandler
    public void playerDamageCheck(EntityDamageEvent e) {
        Entity player = e.getEntity();
        if (player.getType().equals(EntityType.PLAYER)) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame((Player) player)) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                    e.setDamage(6);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                    e.setDamage(0);
                } else if (e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                    e.setDamage(0);
                }
            }
        }
    }

    @EventHandler
    public void checkdamagerFinalDamage(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        if (player.getType().equals(EntityType.PLAYER)) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame((Player) player)) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    if (damager instanceof Arrow) {
                        Arrow arrow = (Arrow) damager;
                        if (arrow.getShooter() instanceof Player) {
                            Player shooter = (Player) arrow.getShooter();
                            killer.put(player, shooter);
                        }
                    } else if (damager instanceof Player) {
                        killer.put(player, damager);
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = killer.get(player);
        killer.remove(player);
        ArenaSettings.playerArena.remove(player);
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(KnockbackFFA.getInstance(), () -> {
                player.spigot().respawn();
                KnockbackFFAArena.teleportPlayertoArena(player);
            }, 1);
            World world = player.getWorld();
            List<Entity> entList = world.getEntities();
            for (Entity current : entList) {
                if (current instanceof Item) {
                    current.remove();
                }
            }
            killStreak.put(player, 0);
            PlayerData.load(player);
            PlayerData.get().set("deaths", PlayerData.get().getInt("deaths") + 1);
            PlayerData.save();
            if (damager != null) {
                    KnockbackFFAAPI.loadCosmetic((Player) damager,KnockbackFFAAPI.selectedCosmetic((Player) damager));

                PlayerData.load((Player) damager);
                float prize = KnockbackFFA.getInstance().getConfig().getInt("killprize");
                damager.sendMessage(MessageConfiguration.get().getString("prize").replace("%prize%",prize+"").replace("&", "§"));
                balanceAPI.addBalance((Player) damager, prize);
                PlayerData.get().set("kills", PlayerData.get().getInt("kills") + 1);
                if (killStreak.get(damager) == null) {
                    killStreak.put(damager, 1);
                } else {
                    killStreak.put(damager, killStreak.get(damager).intValue() + 1);
                }
                if (killStreak.get(damager) > PlayerData.get().getInt("best-ks")) {
                    Player damagerP =(Player) damager;
                    String msg = MessageConfiguration.get().getString("killstreakrecord").replace("%killstreak%", PlayerData.get().getInt("best-ks")+ "");
                    damagerP.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
                    PlayerData.get().set("best-ks", killStreak.get(damager));
                }
                PlayerData.save();
                String deathText = MessageConfiguration.get().getString("deathmessage").replace("%killer%", damager.getName());
                deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
                e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', deathText));
            } else if (damager==null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',MessageConfiguration.get().getString("suicide")));
            }
        }
    }
}
