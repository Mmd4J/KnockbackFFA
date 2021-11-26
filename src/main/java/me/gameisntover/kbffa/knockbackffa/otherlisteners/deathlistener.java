package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlaySoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathListener implements Listener {
    Map<Entity, Integer> killStreak = new HashMap<>();
    @EventHandler
    public void playerDamageCheck(EntityDamageEvent e) {
        Entity player = e.getEntity();
        if (player.getType().equals(EntityType.PLAYER)) {
            if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                    e.setDamage(2);
            }
            if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setDamage(0);
            }  if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                e.setDamage(0);
            }  if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                e.setDamage(0);
            }   if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                e.setDamage(0);
            }   if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                e.setDamage(0);
            }   if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FIRE) || player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                e.setDamage(0);
            }   if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                e.setDamage(0);
            }   if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                e.setDamage(0);
            }   if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                e.setDamage(0);
            }
        }
    }
    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
       Entity killer = player.getKiller();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        int i = scheduler.scheduleSyncDelayedTask(KnockbackFFA.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                KnockbackFFAKit.Kits(player);
                if (ArenaConfiguration.get().getString("EnabledArena").equalsIgnoreCase("arena1")) {
                    if(ArenaConfiguration.get().getString("arena1.world")!=null) {
                        double x = ArenaConfiguration.get().getDouble("arena1.x");
                        double y = ArenaConfiguration.get().getDouble("arena1.y");
                        double z = ArenaConfiguration.get().getDouble("arena1.z");
                        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena1.world"));
                        player.teleport(new Location(world, x, y, z));

                    }else {
                        ArenaConfiguration.get().set("EnabledArena","arena2");

                    }
                } else         if (ArenaConfiguration.get().getString("EnabledArena").equalsIgnoreCase("arena2")) {
                    if(ArenaConfiguration.get().getString("arena2.world")!=null) {
                        double x = ArenaConfiguration.get().getDouble("arena2.x");
                        double y = ArenaConfiguration.get().getDouble("arena2.y");
                        double z = ArenaConfiguration.get().getDouble("arena2.z");
                        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena2.world"));
                        player.teleport(new Location(world, x, y, z));

                    }
                    else {
                        ArenaConfiguration.get().set("EnabledArena","arena3");
                    }
                } else         if (ArenaConfiguration.get().getString("EnabledArena").equalsIgnoreCase("arena3")) {

                    if(ArenaConfiguration.get().getString("arena3.world")!=null) {
                        double x = ArenaConfiguration.get().getDouble("arena3.x");
                        double y = ArenaConfiguration.get().getDouble("arena3.y");
                        double z = ArenaConfiguration.get().getDouble("arena3.z");
                        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena3.world"));
                        player.teleport(new Location(world, x, y, z));

                    }
                    else {
                        ArenaConfiguration.get().set("EnabledArena","arena4");
                    }
                } else        if (ArenaConfiguration.get().getString("EnabledArena").equalsIgnoreCase("arena4")) {

                    if(ArenaConfiguration.get().getString("arena4.world")!=null) {
                        double x = ArenaConfiguration.get().getDouble("arena4.x");
                        double y = ArenaConfiguration.get().getDouble("arena4.y");
                        double z = ArenaConfiguration.get().getDouble("arena4.z");
                        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena4.world"));
                        player.teleport(new Location(world, x, y, z));
                    }
                }
                else {
                    ArenaConfiguration.get().set("EnabledArena","arena1");
                }

            }
        }, 1);        killStreak.put(player, 0);

        PlayerConfiguration.load(player);
        PlayerConfiguration.get().set("deaths",PlayerConfiguration.get().getInt("deaths") + 1);
        PlayerConfiguration.save();
        if(killer!=null) {
            PlayerConfiguration.load((Player) killer);
            PlayerConfiguration.get().set("kills", PlayerConfiguration.get().getInt("kills") + 1);
            PlayerConfiguration.save();
        }
        if (killer == null) {
            player.sendMessage(ChatColor.AQUA + "You died by falling into the void");
        } else{
        String deathText = MessageConfiguration.get().getString("deathmessage").replace("&", "§").replace("%killer%", killer.getName());
        deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
        e.setDeathMessage(deathText);
        if (killStreak.get(killer) == null) {
                killStreak.put(killer, 1);
            } else {killStreak.put(killer, killStreak.get(killer).intValue() + 1);
            if (killStreak.get(killer) == 5) {
                ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 4);
                ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                enderpearl.setItemMeta(enderpearlmeta);
                Player playerKiller = (Player) killer;
                Inventory inv = playerKiller.getInventory();
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.5message").replace("&", "§").replace("%killer%", playerKiller.getDisplayName()));
                inv.addItem(enderpearl);
                playerKiller.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("5kills")), 1, 1);
            } else if (killStreak.get(killer) == 10) {

                ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 8);
                ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                enderpearl.setItemMeta(enderpearlmeta);
                Player playerKiller = (Player) killer;
                Inventory inv = playerKiller.getInventory();
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.10message").replace("&", "§").replace("%killer%", playerKiller.getDisplayName()));
                inv.addItem(enderpearl);
                playerKiller.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("10kills")), 1, 1);
            } else if (killStreak.get(killer) == 15) {
                ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
                ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                enderpearl.setItemMeta(enderpearlmeta);
                Player playerKiller = (Player) killer;
                Inventory inv = playerKiller.getInventory();
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.15message").replace("&", "§").replace("%killer%", playerKiller.getDisplayName()));
                inv.addItem(enderpearl);
                playerKiller.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("15kills")), 1, 1);
            } else if (killStreak.get(killer) >= 15) {
                Player playerKiller = (Player) killer;
                playerKiller.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("+15kills")), 1, 1);
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.lastmessage").replace("&", "§").replace("%killer%", playerKiller.getDisplayName()).replace("%killstreak%", killStreak.get(killer).intValue() - 1 + "kills"));
            }
        }
            }
            }
        }
