package me.gameisntover.kbffa.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gameisntover.kbffa.api.BalanceAPI;
import me.gameisntover.kbffa.Arena.Arena;
import me.gameisntover.kbffa.customconfigs.PlayerData;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

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
        if (params.equalsIgnoreCase("player_balance")){
            return BalanceAPI.getBalance(player.getPlayer())+"";
        }
        if (params.equalsIgnoreCase("current_map")) {
            String arenaName = Arena.enabledArena.getName();
            if (arenaName == null) {
                return "No Arena";
            } else {
                return arenaName;
            }
        }
        if (params.equalsIgnoreCase("timer_nextmap")){
            int timer = KnockbackFFA.getInstance().timer;
            int seconds = timer % 60;
            int minutes = timer / 60;
            return minutes + ":" + seconds;
        }
        if (params.equalsIgnoreCase("next_map")){
            if (Arena.getfolder().list()!=null && Arena.getfolder().list().length>1){
                String arenaName = Arena.getEnabledArena().getName();

                List<String> arenaList = Arrays.asList(Arena.getfolder().list());
                int index = arenaList.indexOf(arenaName);
                if (index == arenaList.size()-1){
                    return arenaList.get(0).replace(".yml","");
                }else {
                    return arenaList.get(index+2).replace(".yml","");
                }
            }else {
                return "No Arena";
            }
        }
        return null;
    }
}
