package me.gameisntover.kbffa.util;

import me.gameisntover.kbffa.api.KnockConfiguration;

public class CosmeticConfiguration extends KnockConfiguration {
    @Override
    public String getName() {
        return "cosmetics";
    }

    @Override
    public String getResourceName() {
        return "cosmetics.yml";
    }

}

