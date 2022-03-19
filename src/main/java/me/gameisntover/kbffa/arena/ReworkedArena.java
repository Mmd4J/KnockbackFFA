package me.gameisntover.kbffa.arena;

import lombok.Data;
import org.bukkit.Location;

@Data
public class ReworkedArena {

    private final String name, displayname;
    private final Location spawn, cornerA, cornerB;

}
