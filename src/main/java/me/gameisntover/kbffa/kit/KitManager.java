package me.gameisntover.kbffa.kit;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
@Getter
public class KitManager {
    private final Map<String, Kit> kitHandler = new HashMap<>();
    private File folder = new File(KnockbackFFA.getINSTANCE().getDataFolder(), "Kits" + File.separator);
    @SneakyThrows
    public Kit create(String kitname, ItemStack[] items, Material icon) {
        if (kitHandler.containsKey(kitname)) return kitHandler.get(kitname);
        else {
            Kit kit = new Kit(kitname,items,icon);
            kitHandler.put(kitname, kit);
            return kit;
        }
    }

    public Kit load(String kitname) {
        if (kitHandler.containsKey(kitname)) return kitHandler.get(kitname);
            else {
                Kit kit = new Kit(kitname);
                kitHandler.put(kitname, kit);
                return kit;
            }
        }
    }
