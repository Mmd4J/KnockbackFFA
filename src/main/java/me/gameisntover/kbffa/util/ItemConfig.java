package me.gameisntover.kbffa.util;

import lombok.Data;
import me.gameisntover.kbffa.api.KnockConfiguration;

@Data
public class ItemConfig extends KnockConfiguration {
    @Override
    public String getName() {
        return "Items";
    }

    @Override
    public String getResourceName() {
        return "Items.yml";
    }
}
