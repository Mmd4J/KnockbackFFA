package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


import java.util.HashMap;
import java.util.Map;

public class deathlistener implements Listener {
    Map<Entity, Integer> killStreak = new HashMap<>();
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
                p.getInventory().clear();
            }

        }

    }

    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = damagers.get(player);
        killStreak.put(player, 0);
        Double x = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.x");
        Double y = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.y");
        Double z = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.z");
        World world = KnockbackFFA.getInstance().getServer().getWorld(KnockbackFFA.getInstance().getConfig().getString("spawnpoint.world"));
        player.spigot().respawn();
        player.teleport(new org.bukkit.Location(world, x, y, z));
        String deathText = KnockbackFFA.getInstance().getConfig().getString("deathmessage").replace("&", "§").replace("%killer%", damager.getName());
        deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
        e.setDeathMessage(deathText);
        damager.sendMessage(ChatColor.GRAY + "You killed " + ChatColor.BOLD + ChatColor.GREEN + player.getDisplayName() + " ");

        if (damager == null) {
            player.sendMessage(ChatColor.AQUA + "You died by falling into the void");
        } else if (damager != null) {
            if (killStreak.get(damager) == null) {
                killStreak.put(damager, 1);
            } else {
                damagers.remove(player);
                killStreak.put(damager, killStreak.get(damager).intValue() + 1);
                if (killStreak.get(damager) == 5) {

                    ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 4);
                    ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                    enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                    enderpearl.setItemMeta(enderpearlmeta);
                    Player killer = (Player) damager;
                    Inventory inv = killer.getInventory();
                    Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.5message").replace("&", "§").replace("%killer%", killer.getDisplayName()));
                    inv.addItem(enderpearl);
                    killer.playSound(killer.getLocation(), "minecraft:entity.wolf.howl", 1, 1);

                } else if (killStreak.get(damager) == 10) {

                    ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 8);
                    ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                    enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                    enderpearl.setItemMeta(enderpearlmeta);
                    Player killer = (Player) damager;
                    Inventory inv = killer.getInventory();
                    Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.10message").replace("&", "§").replace("%killer%", killer.getDisplayName()));
                    inv.addItem(enderpearl);
                    killer.playSound(killer.getLocation(), "minecraft:entity.lightning_bolt.thunder", 1, 1);
                } else if (killStreak.get(damager) == 15) {

                    ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
                    ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                    enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                    enderpearl.setItemMeta(enderpearlmeta);
                    Player killer = (Player) damager;
                    Inventory inv = killer.getInventory();
                    Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.15message").replace("&", "§").replace("%killer%", killer.getDisplayName()));
                    inv.addItem(enderpearl);
                    killer.playSound(killer.getLocation(), "minecraft:ui.toast.challenge_complete", 1, 1);
                } else if (killStreak.get(damager) >= 15) {
                    Player killer = (Player) damager;
                    ArmorStand armorStand = (ArmorStand) killer.getWorld().spawnEntity(killer.getLocation(), EntityType.ARMOR_STAND);
                    armorStand.setInvisible(true);
                    armorStand.setGravity(false);
                    armorStand.setHealth(0.5);
                    ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skullMeta = (SkullMeta) playerhead.getItemMeta();
                    skullMeta.setOwningPlayer(player);
                    armorStand.getEquipment().setHelmet(playerhead);
                    killer.playSound(killer.getLocation(), "minecraft:entity.lightning_bolt.thunder", 1, 1);
                    Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.lastmessage").replace("&", "§").replace("%killer%", killer.getDisplayName()).replace("%killstreak%", killStreak.get(damager).intValue() - 1 + "kills"));
                }
            }
            }
        }
    }