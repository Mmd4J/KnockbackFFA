package me.gameisntover.kbffa.events;


public interface GameEvent {
    void start();

    void end();

    String getName();

    String getDescription();

    int getGameEventDuration();
}
