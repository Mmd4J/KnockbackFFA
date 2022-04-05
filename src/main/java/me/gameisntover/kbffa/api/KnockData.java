package me.gameisntover.kbffa.api;

import lombok.SneakyThrows;

import java.io.File;

public class KnockData {
    @SneakyThrows
    public File loadDataFile(File folder, String name){
        if (!folder.exists()) folder.mkdir();
        File file = new File(folder,name + ".yml");
        if (!file.exists()) file.createNewFile();
        return file;
    }
}
