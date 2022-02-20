package me.gameisntover.kbffa.knockbackffa.Placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Expansion extends PlaceholderExpansion
{
    private final KnockbackFFA plugin;

    public Expansion(KnockbackFFA plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "GaMeIsNtOvEr";
    }

    @Override
    public String getIdentifier() {
        return "advancedknockbackffa";
    }

    @Override
    public String getVersion() {
        return "2.9";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("player_kills")) {
            Player player1 = player.getPlayer();
            PlayerData.load(player1);
            return String.valueOf(PlayerData.get().getInt("kills"));
        }

        if (params.equalsIgnoreCase("player_deaths")) {
            Player player1 = player.getPlayer();
            PlayerData.load(player1);
            return String.valueOf(PlayerData.get().getInt("deaths"));
        }
        if (params.equalsIgnoreCase("current_map")) {
            String arenaName = ArenaConfiguration.get().getString("EnabledArena");
            if (arenaName == null) {
                return "No Arena";
            } else {
                return ArenaConfiguration.get().getString(arenaName + ".name");
            }
        }
        return null;
    }
}
