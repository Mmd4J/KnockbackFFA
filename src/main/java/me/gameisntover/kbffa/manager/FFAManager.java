package me.gameisntover.kbffa.manager;

import me.gameisntover.kbffa.manager.api.SubManager;
import me.gameisntover.kbffa.manager.sub.ArenaManager;

import java.util.Arrays;
import java.util.List;

public class FFAManager {

    private final ArenaManager arenaManager = new ArenaManager();

    public void load() {
        subManagers().forEach(SubManager::onLoad);
    }

    public void unload() {
        subManagers().forEach(SubManager::onUnload);
    }

    public void reload() {
        subManagers().forEach(SubManager::onReload);
    }

    private List<SubManager> subManagers() {
        return Arrays.asList(arenaManager);
    }

}
