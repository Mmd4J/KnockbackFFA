package me.gameisntover.kbffa.bots;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.util.Items;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class BotA extends Bot {
    Mob mob;

    public BotA(String name, Location location) {
        super(name, location);
        Zombie zombie = (Zombie) mob;
        zombie.setAdult();
    }

    @Override
    public ItemStack getItemInHand() {
        return Items.KNOCKBACK_STICK.getItem();
    }

    @Override
    public Mob getMob(Location location) {
        mob = (Mob) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        return mob;
    }

    @Override
    public void onDeath() {
        List<String> stringList = KnockbackFFA.getInstance().getBotManager().getConfig.getStringList("bot-death-messages");
        chat(stringList.get(new Random().nextInt(stringList.size() - 1)));
        super.mob.teleport(KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getSpawnLocation());
        if (lastDamager != null)
            Bukkit.broadcastMessage(Message.DEATH_KNOCKED_GOBAL.toString().replace("%killer%", lastDamager.getName()).replace("%player_name%", mob.getName()));
        else Bukkit.broadcastMessage(Message.DEATH_VOID_GLOBAL.toString().replace("%player_name%", mob.getName()));

    }

    @Override
    public void start() {
        chat(KnockbackFFA.getInstance().getBotManager().getConfig.getStringList("bot.add-messages").get(new Random().nextInt(KnockbackFFA.getInstance().getBotManager().getConfig.getStringList("bot.add-messages").size() - 1)));
    }

    @Override
    public void update() {
        if (mob.getTarget() == null) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().getEnabledArena();
            if (arena.getNearestAIPath(getMob().getLocation()) != null && !getMob().getLocation().getWorld().getBlockAt(new Location(getMob().getLocation().getWorld(), getMob().getLocation().getBlockX(), getMob().getLocation().getBlockY() - 1, getMob().getLocation().getBlockZ())).
                    getType().equals(Material.AIR) && !isInArena())
                getMob().setVelocity(arena.getNearestAIPath(getMob().getLocation()).getDirection());

        }
    }
}
