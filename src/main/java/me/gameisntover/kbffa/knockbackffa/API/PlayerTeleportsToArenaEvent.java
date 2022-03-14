package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.Arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerTeleportsToArenaEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private Player player;
    private boolean isCancelled;
    public PlayerTeleportsToArenaEvent(Player player , Arena arena){
        this.player = player;
        this.arena = arena;
    }
    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public Arena getArena(){
        return arena;
    }
    public Player getPlayer(){
        return player;
    }
}
