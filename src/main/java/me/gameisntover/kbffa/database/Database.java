package me.gameisntover.kbffa.database;

import me.gameisntover.kbffa.api.ReworkedKnocker;

import java.util.UUID;

public interface Database {

    ReworkedKnocker getKnocker(UUID uuid);
    void updateKnocker(ReworkedKnocker knocker);

}
