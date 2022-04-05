package me.gameisntover.kbffa.gameevents;


public interface GameEvent {
    void start();
    void end();
    String getName();
    String getDescription();
    int getGameEventDuration();
}
