package me.gameisntover.kbffa.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.TempArenaManager;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Expansion extends PlaceholderExpansion {
    private final TempArenaManager arenaManager = new TempArenaManager();

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
        return "2.9";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String placeholder) {

        Player player1 = player.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player1);

        switch (placeholder) {
            case "player_kills":
                return String.valueOf(knocker.getConfig().getInt("kills"));
            case "player_deaths":
                return String.valueOf(knocker.getConfig().getInt("deaths"));
            case "player_balance":
                return KnockbackFFA.getINSTANCE().getBalanceAPI().getBalance(knocker) + "";
            case "current_map":
                String arenaName = arenaManager.getEnabledArena().getName();
                if (arenaName == null) return "No Arena";
                else return arenaName;
            case "timer_nextmap":
                int timer = KnockbackFFA.getINSTANCE().getTimer();
                int seconds = timer % 60;
                int minutes = timer / 60;
                return minutes + ":" + seconds;
            case "next_map":
                if (arenaManager.getfolder().list() == null || arenaManager.getfolder().list().length < 1)
                    return "No Arena";
                String currentArenaName = arenaManager.getEnabledArena().getName();
                List<String> arenaList = Arrays.asList(arenaManager.getfolder().list());
                int index = arenaList.indexOf(currentArenaName);
                if (index == arenaList.size() - 1) return arenaList.get(0).replace(".yml", "");
                else return arenaList.get(index + 2).replace(".yml", "");
        }
        return null;
    }
}
