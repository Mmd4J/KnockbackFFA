package me.gameisntover.kbffa.api;

import me.gameisntover.kbffa.Arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ArenaCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private Player player;
    public ArenaCreateEvent(Player player , Arena arena){
        this.player = player;
        this.arena = arena;
    }
    public Arena getArena(){
        return arena;
    }
    public Player getPlayer(){
        return player;
    }
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
