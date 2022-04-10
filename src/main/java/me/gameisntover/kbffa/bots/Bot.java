package me.gameisntover.kbffa.bots;

import lombok.Getter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.util.Items;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public abstract class Bot implements Listener {
    private String name;
    Zombie zombie;
    private boolean inArena;
    private boolean dead;

    public Bot(String name, Location location) {
        assert location.getWorld() != null;
        this.name = name;
        zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.getEquipment().setItemInMainHand(Items.KNOCKBACK_STICK.getItem());
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
        zombie.getEquipment().setHelmet(head);
        zombie.getEquipment().setChestplate(leatherChest);
        zombie.getEquipment().setLeggings(leatherLeg);
        zombie.getEquipment().setBoots(leatherBoot);
        // --------------------------------------
        zombie.setRemoveWhenFarAway(false);
        zombie.setCanPickupItems(true);
        zombie.setAdult();
        zombie.setTarget(null);
        zombie.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        KnockbackFFA.getINSTANCE().getServer().getPluginManager().registerEvents(this, KnockbackFFA.getINSTANCE());
        start();
        dead = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isDead()) update();
                else cancel();
            }
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 20, 20);
    }

    public void attackPlayer(Knocker knocker) {
        Location location = knocker.getPlayer().getLocation();
        zombie.setTarget(knocker.getPlayer());
        zombie.setVelocity(location.getDirection());
    }

    public void jump() {
        zombie.setVelocity(zombie.getLocation().getDirection().setY(0.5));
    }

    public void setName(String name) {
        this.name = name;
        zombie.setCustomName(name);
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() != zombie) return;
        if (!inArena) e.setCancelled(true);
        else if (!e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) e.setDamage(0);
    }

    @EventHandler
    public void onDeathEvent(EntityDeathEvent e) {
        if (e.getEntity() == zombie) dead = true;
    }

    public abstract void start();

    public abstract void update();
}
