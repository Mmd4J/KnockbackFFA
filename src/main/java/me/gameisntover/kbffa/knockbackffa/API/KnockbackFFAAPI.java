package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.entity.Player;

public class KnockbackFFAAPI {
    public static boolean BungeeMode(){
       return KnockbackFFA.getInstance().getConfig().getBoolean("Bungee-Mode");
    }
    public static boolean isInGame(Player playername){
        PlayerData.load(playername);
        return PlayerData.get().getBoolean("In-Game");
    }
}
