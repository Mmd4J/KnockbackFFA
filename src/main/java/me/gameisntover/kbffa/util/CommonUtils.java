package me.gameisntover.kbffa.util;

import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.entity.Player;

public class CommonUtils {


    public static void giveLobbyItems(Player player) {
        player.getInventory().setItem(KnockbackFFA.getInstance().getItems().getConfig.getInt("LobbyItems.shop.slot"), Items.SHOP_ITEM.getItem());
        player.getInventory().setItem(KnockbackFFA.getInstance().getItems().getConfig.getInt("LobbyItems.cosmetic.slot"), Items.COSMETIC_ITEM.getItem());
        player.getInventory().setItem(KnockbackFFA.getInstance().getItems().getConfig.getInt("LobbyItems.kits.slot"), Items.KIT_ITEM.getItem());
    }

}
