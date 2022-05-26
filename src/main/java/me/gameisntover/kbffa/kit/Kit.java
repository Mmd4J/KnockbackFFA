package me.gameisntover.kbffa.kit;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Kit {
    private File file;
    private FileConfiguration config;
    private File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "Kits" + File.separator);
    private File df = KnockbackFFA.getInstance().getDataFolder();
    private String kitName;
    private Material icon;
    private double price;

    @SneakyThrows
    public Kit(String kitsName) {
        this.kitName = kitsName;
        if (!df.exists()) df.mkdir();
        if (!folder.exists()) folder.mkdir();
        file = new File(df, "Kits" + File.separator + kitsName + ".yml");
        if (!file.exists()) file.createNewFile();
        config = YamlConfiguration.loadConfiguration(file);
        icon = Material.getMaterial(getConfig().getString("KitIcon"));
        price = getConfig().getDouble("Price");
    }

    @SneakyThrows
    public Kit(String kitsName, ItemStack[] items, Material icon) {
        this.kitName = kitsName;
        if (!df.exists()) df.mkdir();
        if (!folder.exists()) folder.mkdir();
        file = new File(df, "Kits" + File.separator + kitsName + ".yml");
        if (!file.exists()) file.createNewFile();
        config = YamlConfiguration.loadConfiguration(file);
        Arrays.stream(items).forEach(itemStack -> itemStack.setType(itemStack == null ? Material.AIR : itemStack.getType()));
        getConfig().set("KitContents", items);
        getConfig().set("Price", 100);
        getConfig().set("KitIcon", icon != null && icon != Material.AIR ? icon.name() : "BARRIER");
        getConfig().set("KitDescription", Arrays.asList(ChatColor.GRAY + "Another cool kit!", ChatColor.GRAY + "Must be configured in plugins/KnockbackFFA/kits !"));
        save();
        this.icon = Material.getMaterial(getConfig().getString("KitIcon"));
        price = getConfig().getDouble("Price");
    }

    public void giveKit(Player player) {
        if (getConfig().getList("KitContents") != null) {
            List<ItemStack> kitContents = Arrays.asList(getConfig().getList("KitContents").toArray(new ItemStack[0]));
            player.getInventory().setContents(kitContents.toArray(new ItemStack[0]));
            for (ItemStack item : kitContents) {
                if (item.getType().toString().contains("Helmet")) player.getInventory().setHelmet(item);
                if (item.getType().toString().contains("Chestplate")) player.getInventory().setChestplate(item);
                if (item.getType().toString().contains("Leggings")) player.getInventory().setLeggings(item);
                if (item.getType().toString().contains("Boots")) player.getInventory().setBoots(item);
            }
        } else {
            ReworkedKnocker knocker = ReworkedKnocker.get(player.getUniqueId());
            knocker.getOwnedKits().addAll(knocker.getOwnedKits().stream().filter(s -> s.contains(kitName)).collect(Collectors.toList()));
        }
    }

    @SneakyThrows
    public void save() {
        config.save(file);
    }
}