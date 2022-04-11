package me.gameisntover.kbffa.bots;

import lombok.Getter;
import me.gameisntover.kbffa.api.KnockConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BotManager extends KnockConfiguration {
    private Map<String, Bot> botHandler = new HashMap<>();

    @Override
    public String getName() {
        return "bot-config";
    }

    @Override
    public String getResourceName() {
        return "bot-config.yml";
    }
}
