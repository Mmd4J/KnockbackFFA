package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ItemConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.Listeners.ArenaSettings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

public class GameRules implements Listener
{
    Integer arenaID = 1;
    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        if (KnockbackFFAAPI.isInGame(e.getPlayer()) || KnockbackFFAAPI.BungeeMode()) {
            Player player = e.getPlayer();
            new BukkitRunnable()
            {
                @Override
                public void run() {
                    int vd = 1;
                    if (ArenaConfiguration.get().getLocation("voids." + vd + ".pos1") != null) {
                        Location pos1 = ArenaConfiguration.get().getLocation("voids." + vd + ".pos1");
                        Location pos2 = ArenaConfiguration.get().getLocation("voids." + vd + ".pos2");
                        BoundingBox bb = new BoundingBox(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
                        if (bb.contains(player.getLocation().toVector()) && player.getWorld() == pos1.getWorld()) {

                            Integer damage = ArenaConfiguration.get().getInt("voids." + vd + ".damage");
                            if (damage != null) {
                                player.damage(damage);
                                player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, damage));
                                if (player.isDead()) {
                                    cancel();
                                }

                            } else {
                                throw new NullPointerException("Void " + vd + " damage is null");
                            }
                        } else {
                            arenaID++;
                        }
                    } else {
                        arenaID++;
                    }
                    if (ArenaConfiguration.get().getLocation("voids." + vd + ".pos1") == null) {
                        cancel();
                    }
                }
            }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);
            new BukkitRunnable()
            {
                @Override
                public void run() {

                    if (ArenaConfiguration.get().getString("Safezones." + arenaID + ".Safezone.world") != null) {
                        BoundingBox s1box = new BoundingBox(ArenaConfiguration.get().getDouble("Safezones." + arenaID + ".Safezone.pos1.x"), ArenaConfiguration.get().getDouble("Safezones." + arenaID + ".Safezone.pos1.y"), ArenaConfiguration.get().getDouble("Safezones." + arenaID + ".Safezone.pos1.z"), ArenaConfiguration.get().getDouble("Safezones." + arenaID + ".Safezone.pos2.x"), ArenaConfiguration.get().getDouble("Safezones." + arenaID + ".Safezone.pos2.y"), ArenaConfiguration.get().getDouble("Safezones." + arenaID + ".Safezone.pos2.z"));
                        World s1world = Bukkit.getWorld(ArenaConfiguration.get().getString("Safezones." + arenaID + ".Safezone.world"));
                        Player player = e.getPlayer();
                        Location location = player.getLocation();
                        if (s1box.contains(location.toVector()) && player.getWorld() == s1world) {
                            player.setInvulnerable(true);
                            cancel();
                            arenaID = 1;
                        } else {
                            player.setInvulnerable(false);
                            arenaID++;
                        }
                    } else if (ArenaConfiguration.get().getString("Safezones." + arenaID + ".Safezone.world") == null) {
                        cancel();
                        arenaID = 1;
                    }
                }
            }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
        }
    }

    @EventHandler
    public void onArrowOnGround(PlayerPickupArrowEvent e) {
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBowUse(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.BOW)) {
                    new BukkitRunnable()
                    {
                        int timer = 10;

                        @Override
                        public void run() {
                            timer--;
                            if (timer == 10 || timer == 9 || timer == 8 || timer == 7 || timer == 6 || timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
                                if (player.getInventory().contains(Material.ARROW)) {
                                    cancel();
                                    timer = 10;
                                } else {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageConfiguration.get().getString("bowuse").replace("%timer%", String.valueOf(timer)).replace("%player%", player.getName()).replace("&", "§")));
                                }
                            }
                            if (timer == 0) {
                                if (player.getInventory().contains(Material.ARROW)) {
                                    cancel();
                                    timer = 10;
                                } else {
                                    KnockbackFFAKit kitManager = new KnockbackFFAKit();
                                    player.getInventory().addItem(kitManager.kbbowArrow());
                                    cancel();
                                    timer = 10;
                                }
                            }
                        }
                    }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);
                }
            }
        }
    }
}
