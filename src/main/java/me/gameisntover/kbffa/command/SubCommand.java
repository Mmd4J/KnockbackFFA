package me.gameisntover.kbffa.command;

import me.gameisntover.kbffa.customconfig.Knocker;

import java.util.List;


public abstract class SubCommand {
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract void perform(Knocker knocker , String[] args);

    public abstract List<String> tabComplete(Knocker knocker, String[] args);

}
