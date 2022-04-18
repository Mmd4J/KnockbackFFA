package me.gameisntover.kbffa.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Expansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "GaMeIsNtOvEr";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedknockbackffa";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.9.6";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String placeholder) {
        Player player1 = player.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player1);
        if (placeholder.equalsIgnoreCase("player_kills")) return knocker.getConfig().getInt("kills") + "";
        if (placeholder.equalsIgnoreCase("player_deaths")) return knocker.getConfig().getInt("deaths") + "";
        if (placeholder.equalsIgnoreCase("player_balance"))
            return knocker.getBalance() + "";
        if (placeholder.equalsIgnoreCase("current_map")) {
            String arenaName = KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName();
            if (arenaName == null) return "No Arena";
            else return arenaName;
        }
        if (placeholder.equalsIgnoreCase("timer_nextmap")) {
            int timer = KnockbackFFA.getINSTANCE().getTimer();
            int seconds = timer % 60;
            int minutes = timer / 60;
            return minutes + ":" + seconds;
        }
        if (placeholder.equalsIgnoreCase("next_map")) {
            if (KnockbackFFA.getINSTANCE().getArenaManager().getFolder().list() == null || KnockbackFFA.getINSTANCE().getArenaManager().getFolder().list().length <= 1)
                return "No Arena";
            String currentArenaName = KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName();
            List<String> arenaList = Arrays.asList(KnockbackFFA.getINSTANCE().getArenaManager().getFolder().list());
            int index = arenaList.indexOf(currentArenaName);
            if (index == arenaList.size() - 1) return arenaList.get(0).replace(".yml", "");
            else return arenaList.get(index + 2).replace(".yml", "");
        }
        return "Invalid Placeholder";
    }
}
