package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.CosmeticConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.SoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KnockbackFFAAPI
{
   private static Map<UUID , Boolean> inGamePlayer = new HashMap<>();
    public static boolean BungeeMode() {
        return KnockbackFFA.getInstance().getConfig().getBoolean("Bungee-Mode");
    }

    public static boolean isInGame(Player player) {
        return inGamePlayer.get(player.getUniqueId());
    }
    public static void setInGamePlayer(Player player,boolean inGame) {
        inGamePlayer.put(player.getUniqueId(), inGame);
    }
    public static String selectedCosmetic(Player player) {
        PlayerData.load(player);
        return PlayerData.get().getString("selected-cosmetic");
    }
    public static String selectedKit(Player player) {
    PlayerData.load(player);
    return PlayerData.get().getString("selected-kit");
}
    public static void loadCosmetic(Player player,String cosmeticName) {
        String cosmeticType = CosmeticConfiguration.get().getString(cosmeticName + ".type");
        if (cosmeticType != null) {
            if (cosmeticType.equalsIgnoreCase("KILL_PARTICLE")) {
                player.spawnParticle(Particle.valueOf(CosmeticConfiguration.get().getString(cosmeticName + ".effect-type")), player.getLocation(), CosmeticConfiguration.get().getInt(cosmeticName + ".amount"));
            } else if (cosmeticType.equalsIgnoreCase("KILL_EFFECT")) {
                player.playEffect(EntityEffect.valueOf(CosmeticConfiguration.get().getString(cosmeticName + ".effect-type")));
            }
            if (CosmeticConfiguration.get().getString("sound") != null) {
                player.playSound(player.getLocation(), Sound.valueOf(CosmeticConfiguration.get().getString(cosmeticName + ".sound")), CosmeticConfiguration.get().getInt(cosmeticName + ".volume"), CosmeticConfiguration.get().getInt(cosmeticName + ".pitch"));
            }
        }
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
