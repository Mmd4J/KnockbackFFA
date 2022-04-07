package me.gameisntover.kbffa.util;


import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Sound;

public enum Sounds {
    ARENA_CHANGE("BLOCK_NOTE_BLOCK_HAT", "arenachange"),
    ITEM_REMOVED("BLOCK_NOTE_BLOCK_BASS", "itemremoved"),
    JUMP_PLATE("ENTITY_BAT_TAKEOFF", "jumpplate"),
    PLAYER_JOIN("ENTITY_PLAYER_LEVELUP", "playerjoin"),
    GUI_CLOSE("BLOCK_ENDER_CHEST_CLOSE", "guiclose"),
    GUI_OPEN("BLOCK_ENDER_CHEST_OPEN", "guiclose");
    private final String soundname, path;

    Sounds(String soundName, String path) {
        this.soundname = soundName;
        this.path = path;
    }

    public Sound toSound() {
        if (KnockbackFFA.getINSTANCE().getSounds().isSet(path))
            return Sound.valueOf(KnockbackFFA.getINSTANCE().getSounds().getString(path));
            else return Sound.valueOf(soundname);

    }
}