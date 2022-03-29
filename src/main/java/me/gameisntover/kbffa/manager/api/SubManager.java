package me.gameisntover.kbffa.manager.api;

public interface SubManager {

    void onLoad();

    default void onReload() {
        onUnload();
        onLoad();
    }

    void onUnload();
}
