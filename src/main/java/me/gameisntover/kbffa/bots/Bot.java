package me.gameisntover.kbffa.bots;

import lombok.Getter;
import lombok.Setter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Random;

@Getter
@Setter
public abstract class Bot implements Listener {
    Mob mob;
    Player lastDamager;
    private String name;
    private boolean inArena;

    public Bot(String name, Location location) {
        assert location.getWorld() != null;
        this.name = name;
        mob = getMob(location);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        head.setItemMeta(skullMeta);
        ItemStack leatherChest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherChestMeta = (LeatherArmorMeta) leatherChest.getItemMeta();
        leatherChestMeta.setColor(Color.RED);
        leatherChest.setItemMeta(leatherChestMeta);
        ItemStack leatherLeg = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLegMeta = (LeatherArmorMeta) leatherLeg.getItemMeta();
        leatherLegMeta.setColor(Color.RED);
        leatherLeg.setItemMeta(leatherLegMeta);
        ItemStack leatherBoot = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootMeta = (LeatherArmorMeta) leatherBoot.getItemMeta();
        leatherBootMeta.setColor(Color.RED);
        leatherBoot.setItemMeta(leatherBootMeta);
        //applying equipments
        mob.getEquipment().setHelmet(head);
        mob.getEquipment().setChestplate(leatherChest);
        mob.getEquipment().setLeggings(leatherLeg);
        mob.getEquipment().setBoots(leatherBoot);
        mob.getEquipment().setItemInMainHand(getItemInHand());
        // --------------------------------------
        mob.setRemoveWhenFarAway(false);
        mob.setCanPickupItems(true);
        mob.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, KnockbackFFA.getINSTANCE().getBotManager().getConfig.getInt("bot-speed")));
        mob.setTarget(null);
        mob.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        mob.setCustomNameVisible(true);
        KnockbackFFA.getINSTANCE().getServer().getPluginManager().registerEvents(this, KnockbackFFA.getINSTANCE());
        mob.setMetadata("bot", new FixedMetadataValue(KnockbackFFA.getINSTANCE(), "bot-" + mob.getUniqueId()));
        mob.setSilent(true);
        start();
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
                setInArena(KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().contains(mob.getLocation()));
            }
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 20, 20);
    }

    public abstract ItemStack getItemInHand();

    public abstract Mob getMob(Location location);

    public void remove() {
        HandlerList.unregisterAll(this);
        mob.teleport(new Location(mob.getWorld(), 0, -20, 0));
        mob.setHealth(0);
    }

    public void attackPlayer(Knocker knocker) {
        Location location = knocker.getPlayer().getLocation();
        mob.setTarget(knocker.getPlayer());
        mob.setVelocity(location.getDirection());
    }

    public void jump() {
        mob.setVelocity(mob.getLocation().getDirection().setY(0.5));
    }

    public void setName(String name) {
        this.name = name;
        mob.setCustomName(name);
    }

    public void chat(String message) {
        Bukkit.broadcastMessage(Message.CHATFORMAT.toString().replace("%player%", getName()).replace("%message%", message));
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() != mob) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            if (mob.getHealth() - e.getDamage() <= 0) {
                mob.setHealth(20);
                onDeath();
                e.setCancelled(true);
            }
        } else {
            if (!isInArena()) e.setCancelled(true);
            else e.setDamage(0);
        }
    }

    @EventHandler
    public void onMobDamageEvent(EntityDamageByEntityEvent e) {
        //Deflection method
        if (e.getEntity() != mob) return;
        if (e.getDamager() instanceof Player) lastDamager = (Player) e.getDamager();
        if (!isInArena()) return;
        if (Arrays.asList(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK, EntityDamageEvent.DamageCause.ENTITY_ATTACK).contains(e.getCause())) {
            Random random = new Random();
            if (random.nextInt(4) == 2) mob.launchProjectile(EnderPearl.class, mob.getVelocity());
            //Don't ask why i used integer here instead of nextBoolean since it would be really unfair for players...
        }
    }


    public abstract void onDeath();


    @EventHandler
    public void onTargetEvent(EntityTargetEvent e) {
        if (e.getEntity().equals(mob)) e.setCancelled(!isInArena());
    }

    @EventHandler
    public void onTargetEntityEvent(EntityTargetLivingEntityEvent e) {
        if (e.getEntity().equals(mob)) e.setCancelled(!isInArena());
    }

    @EventHandler
    public void onEntityPressurePlate(EntityInteractEvent e) {
        if (e.getEntity() != mob) return;
        if (e.getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
            Block block = e.getBlock();
            block.getDrops().clear();
            mob.setVelocity(mob.getLocation().getDirection().setY(KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("SpecialItems.JumpPlate.jumpLevel")));
            mob.getWorld().playSound(mob.getLocation(), Sounds.JUMP_PLATE.toSound(), 1, 1);
        }
    }

    public abstract void start();

    public abstract void update();
}
