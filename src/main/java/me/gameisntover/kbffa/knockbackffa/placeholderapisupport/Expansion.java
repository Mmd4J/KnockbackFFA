package me.gameisntover.kbffa.knockbackffa.placeholderapisupport;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Expansion extends PlaceholderExpansion {

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
        return "AdvancedKnockbackFFA";
    }

    @Override
    public String getVersion() {
        return "2.2";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("player_kills")){
        Player player1 = player.getPlayer();
            PlayerConfiguration.load(player1);
            return String.valueOf(PlayerConfiguration.get().getInt("kills"));
        }

        if(params.equalsIgnoreCase("player_deaths")) {
            Player player1 = player.getPlayer();
            PlayerConfiguration.load(player1);
            return String.valueOf(PlayerConfiguration.get().getInt("deaths"));
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
