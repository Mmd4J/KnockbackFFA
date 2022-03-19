package me.gameisntover.kbffa.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.BalanceAPI;
import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.customconfig.PlayerData;
import me.gameisntover.kbffa.message.Message;
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
    ArenaManager arenaManager;
    @EventHandler
    public void playerDamageCheck(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (player.getType().equals(EntityType.PLAYER)) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setDamage(KnockbackFFA.getInstance().getConfig().getInt("default-void-damage"));
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
        if (!player.getType().equals(EntityType.PLAYER)) return;
            if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame((Player) player)) return;
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
        killer.remove(player);
        KnockbackFFAAPI.setInArenaPlayer(player, false);
        if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(player)) return;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.spigot().respawn();
                    KnockbackFFAKit kitManager = new KnockbackFFAKit();
                    kitManager.lobbyItems(player);
                    arenaManager.teleportPlayerToArena(player);
                    cancel();
                }
            }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
            World world = player.getWorld();
            List<Entity> entList = world.getEntities();
            for (Entity current : entList) {
                if (!(current instanceof Item)) return;
                    current.remove();
            }
            killStreak.put(player, 0);
            PlayerData.load(player);
            PlayerData.get().set("deaths", PlayerData.get().getInt("deaths") + 1);
            PlayerData.save();
            if (damager != null && damager != player) {
                KnockbackFFAAPI.loadCosmetic((Player) damager, KnockbackFFAAPI.selectedCosmetic((Player) damager));

                PlayerData.load((Player) damager);
                float prize = KnockbackFFA.getInstance().getConfig().getInt("killprize");
                damager.sendMessage(Message.PRIZE.toString().replace("%prize%", prize + "").replace("&", "§"));
                BalanceAPI.addBalance((Player) damager, prize);
                PlayerData.get().set("kills", PlayerData.get().getInt("kills") + 1);
                killStreak.merge(damager, 1, Integer::sum);
                if (killStreak.get(damager) > PlayerData.get().getInt("best-ks")) {
                    Player damagerP = (Player) damager;
                    String msg = Message.KILLSTREAK_RECORD.toString().replace("%killstreak%", PlayerData.get().getInt("best-ks") + "");
                    damagerP.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
                    PlayerData.get().set("best-ks", killStreak.get(damager));
                }
                PlayerData.save();
                String deathText = Message.DEATH_KNOCKED_GOBAL.toString().replace("%killer%", damager.getName());
                deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
                e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', deathText));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Message.DEATH_VOID.toString()));
                e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Message.DEATH_VOID_GLOBAL.toString()).replace("%player_name%", player.getName()));
            }
        }
}
