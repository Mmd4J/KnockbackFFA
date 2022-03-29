package me.gameisntover.kbffa.util;

import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.ChatColor;

public enum Message {
    //System related
    CHATFORMAT("&7[&8%player%&7]&f: %message%", "chat-format"),
    SPAWNPOINT_NOT_SET("§cAsk an admin to create the spawnpoint so i can get you there!",
            "no-spawnpoint"),
    ITEM_CLEAR("&aCleared &ball items on the ground", "item-clear"),
    CAN_NOT_LEAVE("&cYou cannot use this command! maybe you are not in the game or bungeemode is on?"
            , "cannot-leave"),
    ALREADY_INGAME("&cYou're already in-game aren't you?", "already-ingame"),
    //Arena related
    ARENA_JOIN("&eYou have joined the arena!", "join-arena"),
    ARENA_LEAVE("&cYou have joined the arena!", "leave-arena"),
    ARENA_CHANGE("&eThe arena has changed to &b%arena%", "arena-change"),
    NO_READY_ARENA("&c&lSorry! &6but there is no ready arenas to join", "no-arenas-ready"),
    // Cosmetics
    COSMETIC_PURCHASE_SUCCESS("&aYou have successfully purchased %cosmetic%"
            , "cosmetics.purchase-success"),
    COSMETIC_NO_ENOUGH_MONEY("&cYou don't have enough money to purchase this item!",
            "cosmetics.no-enough-money"),
    COSMETIC_ALREADY_OWNED("&cYou already own this item!", "cosmetics.already-owned"),
    // in-game messages
    PRIZE("&a+ &f%prize%", "prize"),
    DEATH_KNOCKED("&c You were killed by &a %killer%", "death.by-player"),
    DEATH_KNOCKED_GOBAL("&c %player_name%&e was killed by &a %killer%",
            "death.by-player-global"),
    DEATH_VOID("&bYou fell into the void.", "death.by-void"),
    DEATH_VOID_GLOBAL("&c %player_name% &e fell in to the void.", "death.by-void-global"),
    KILLSTREAK_RECORD("&c You've beaten your previous record of %killstreak% kills!"
            , "killstreak-record"),
    BOW_COOLDOWN("&a You have used your bow! you need to wait %timer% seconds to use it again!",
            "cooldown.bow");

    private final String defaultMessage, path;

    Message(String defaultMessage, String path) {
        this.defaultMessage = defaultMessage;
        this.path = path;
    }

    @Override
    public String toString() {
        if (KnockbackFFA.getINSTANCE().getMessages().isSet(path)) {
            return KnockbackFFA.getINSTANCE().getMessages().getString(ChatColor.translateAlternateColorCodes('&', path));
        }
        return ChatColor.translateAlternateColorCodes('&', defaultMessage);
    }

}
