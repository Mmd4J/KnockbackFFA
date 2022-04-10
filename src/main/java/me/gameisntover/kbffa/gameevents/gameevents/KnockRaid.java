package me.gameisntover.kbffa.gameevents.gameevents;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.gameevents.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;

public class KnockRaid implements GameEvent {
    private Skeleton skeleton;

    @Override
    public void start() {
        Arena arena = KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena();
        skeleton = (Skeleton) arena.getSpawnLocation().getWorld().spawnEntity(arena.getSpawnLocation(), EntityType.PILLAGER);
        skeleton.setAbsorptionAmount(1000);
        double maxhp = skeleton.getHealth() + skeleton.getAbsorptionAmount();
        BossBar bossBar = Bukkit.createBossBar("SkeletonKing", BarColor.PINK, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        bossBar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
        for (Player player : Bukkit.getOnlinePlayers()) bossBar.addPlayer(player);
        while (bossBar.getProgress() > 0) bossBar.setProgress(skeleton.getHealth() / maxhp);

    }

    @Override
    public void end() {
        if (!skeleton.isDead()) skeleton.setHealth(0);
        for (Player player : Bukkit.getOnlinePlayers()) {

        }
    }

    @Override
    public String getName() {
        return "KnockRaid";
    }

    @Override
    public String getDescription() {
        return "Pillagers are comming RUN!s";
    }

    @Override
    public int getGameEventDuration() {
        return 1000;
    }
}
