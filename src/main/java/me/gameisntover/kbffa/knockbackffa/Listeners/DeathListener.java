package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlaySoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

public class DeathListener implements Listener {
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
    public void checkdamagerFinalDamage(EntityDamageByEntityEvent e){
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        if (player.getType().equals(EntityType.PLAYER)) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame((Player) player)) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)||e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)||e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                killer.put(player, damager);
            }
            }
        }
    }
    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = killer.get(player);
        killer.remove(player);
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            int i = scheduler.scheduleSyncDelayedTask(KnockbackFFA.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.spigot().respawn();
                    KnockbackFFAArena.teleportPlayertoArena(player);
                }
            }, 1);
            killStreak.put(player, 0);
            PlayerData.load(player);
            PlayerData.get().set("deaths", PlayerData.get().getInt("deaths") + 1);
            PlayerData.save();
            if (damager != null) {
                PlayerData.load((Player) damager);
                PlayerData.get().set("kills", PlayerData.get().getInt("kills") + 1);
                PlayerData.save();
                String deathText = MessageConfiguration.get().getString("deathmessage").replace("&", "§").replace("%killer%", damager.getName());
                deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
                e.setDeathMessage(deathText);
                if (killStreak.get(damager) == null) {
                    killStreak.put(damager, 1);
                } else {
                    killStreak.put(damager, killStreak.get(damager).intValue() + 1);
                    if (killStreak.get(damager) == 5) {
                        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 4);
                        ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                        enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                        enderpearl.setItemMeta(enderpearlmeta);
                        Player playerdamager = (Player) damager;
                        Inventory inv = playerdamager.getInventory();
                        Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.5message").replace("&", "§").replace("%damager%", playerdamager.getDisplayName()));
                        inv.addItem(enderpearl);
                        playerdamager.playSound(damager.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("5kills")), 1, 1);
                    } else if (killStreak.get(damager) == 10) {

                        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 8);
                        ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                        enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                        enderpearl.setItemMeta(enderpearlmeta);
                        Player playerdamager = (Player) damager;
                        Inventory inv = playerdamager.getInventory();
                        Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.10message").replace("&", "§").replace("%damager%", playerdamager.getDisplayName()));
                        inv.addItem(enderpearl);
                        playerdamager.playSound(damager.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("10kills")), 1, 1);
                    } else if (killStreak.get(damager) == 15) {
                        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
                        ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                        enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                        enderpearl.setItemMeta(enderpearlmeta);
                        Player playerdamager = (Player) damager;
                        Inventory inv = playerdamager.getInventory();
                        Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.15message").replace("&", "§").replace("%damager%", playerdamager.getDisplayName()));
                        inv.addItem(enderpearl);
                        playerdamager.playSound(damager.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("15kills")), 1, 1);
                    } else if (killStreak.get(damager) >= 15) {
                        Player playerdamager = (Player) damager;
                        playerdamager.playSound(damager.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("+15kills")), 1, 1);
                        Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.lastmessage").replace("&", "§").replace("%damager%", playerdamager.getDisplayName()).replace("%killstreak%", killStreak.get(damager).intValue() - 1 + "kills"));
                    }

                }
            }
            else if (damager == null) {

                player.sendMessage(ChatColor.AQUA + "You died by falling into the void");
            } else {
                }
            }
        }
    }
