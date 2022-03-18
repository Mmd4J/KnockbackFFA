package me.gameisntover.kbffa.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static void playSound(Player player, String soundLocation, float volume, float pitch) {
        player.playSound(player.getLocation(), Sound.valueOf(me.gameisntover.kbffa.customconfig.SoundConfiguration.get().getString(soundLocation)), volume, pitch);
    }

}
