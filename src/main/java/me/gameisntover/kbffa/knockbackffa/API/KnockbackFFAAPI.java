package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.SoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class KnockbackFFAAPI
{
    public static boolean BungeeMode() {
        return KnockbackFFA.getInstance().getConfig().getBoolean("Bungee-Mode");
    }

    public static boolean isInGame(Player playername) {
        PlayerData.load(playername);
        return PlayerData.get().getBoolean("In-Game");
    }

    public static boolean isLegacyVersion() {
        return Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12");
    }

    public static void playSound(Player player, String soundLocation, float volume, float pitch) {
        if (isLegacyVersion()) {
            player.playSound(player.getLocation(), Sound.valueOf(SoundConfiguration.get().getString("Legacy." + soundLocation)), volume, pitch);
        } else {
            player.playSound(player.getLocation(), Sound.valueOf(SoundConfiguration.get().getString(soundLocation)), volume, pitch);
        }
    }
}
