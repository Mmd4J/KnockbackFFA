package me.gameisntover.kbffa.util;

import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public enum Items {
    POSITION_SELECTOR_WAND(new ItemBuilder(Material.BLAZE_ROD, 1, ChatColor.GOLD + "Position Selector Wand", Arrays.asList("Use this wand to select positions!",
            "LEFTCLICK = First Position", "RIGHTCLICK = Second position")).create(Enchantment.MENDING, 1, ItemFlag.HIDE_ENCHANTS)),
    COSMETIC_ITEM(new ItemBuilder(Material.getMaterial(KnockbackFFA.getINSTANCE().getItems().getConfig.getString("LobbyItems.cosmetic.material")), 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("LobbyItems.cosmetic.name")), KnockbackFFA.getINSTANCE().getItems().getConfig.getStringList("LobbyItems.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList())).create(),
            new ItemBuilder(Material.CHEST, 1, ChatColor.DARK_PURPLE + "Cosmetics Menu", Collections.singletonList(ChatColor.GRAY + "Click to open the cosmetics menu!")).create()),
    KIT_ITEM(new ItemBuilder(Material.getMaterial(KnockbackFFA.getINSTANCE().getItems().getConfig.getString("LobbyItems.kits.material")), 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("LobbyItems.kits.name")), KnockbackFFA.getINSTANCE().getItems().getConfig.getStringList("LobbyItems.kits.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList())).create(ItemFlag.HIDE_ATTRIBUTES),
            new ItemBuilder(Material.DIAMOND_SWORD, 1, ChatColor.AQUA + "Kits Menu", Collections.singletonList(ChatColor.GRAY + "Click to select kits menu!")).create(ItemFlag.HIDE_ATTRIBUTES)),
    SHOP_ITEM(new ItemBuilder(Material.getMaterial(KnockbackFFA.getINSTANCE().getItems().getConfig.getString("LobbyItems.shop.material")), 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("LobbyItems.shop.name")), KnockbackFFA.getINSTANCE().getItems().getConfig.getStringList("LobbyItems.shop.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList())).create(ItemFlag.HIDE_ATTRIBUTES),
            new ItemBuilder(Material.EMERALD, 1, ChatColor.AQUA + "Shop", Collections.singletonList(ChatColor.GRAY + "Click to open shop menu!")).create()),
    KNOCKBACK_STICK(new ItemBuilder(Material.STICK, 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("SpecialItems.KBStick.name")), Collections.singletonList("")).create(Enchantment.KNOCKBACK, KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("SpecialItems.KBStick.kb-level")),
            new ItemBuilder(Material.STICK, 1, ChatColor.AQUA + "Knockback Stick", Collections.singletonList("")).create(Enchantment.KNOCKBACK, 3)),
    BUILDING_BLOCK(new ItemBuilder(Material.WHITE_WOOL, 1, ChatColor.WHITE + "Building Block", Collections.singletonList("")).create()),
    JUMP_PLATE(new ItemBuilder(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("SpecialItems.JumpPlate.name")), Collections.singletonList("")).create(),
            new ItemBuilder(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1, ChatColor.YELLOW + "Jump Plate", Collections.singletonList("")).create()),
    ENDER_PEARL(new ItemBuilder(Material.ENDER_PEARL, 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("SpecialItems.EnderPearl.name")), Collections.singletonList("")).create(),
            new ItemBuilder(Material.ENDER_PEARL, 1, ChatColor.DARK_GREEN + "Ender Pearl", Collections.singletonList("")).create()),
    BOW(new ItemBuilder(Material.BOW, 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("SpecialItems.KBBow.name")), Collections.singletonList("")).create(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, Enchantment.ARROW_KNOCKBACK, KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("SpecialItems.KBBow.kb-level")),
            new ItemBuilder(Material.BOW, 1, ChatColor.AQUA + "Bow", Collections.singletonList("")).create(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, Enchantment.ARROW_KNOCKBACK, 2)),
    ARROW(new ItemBuilder(Material.ARROW, 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("SpecialItems.KBArrow.name")), Collections.singletonList("")).create(),
            new ItemBuilder(Material.ARROW, 1, ChatColor.AQUA + "Arrow", Collections.singletonList("")).create());

    private final ItemStack item;
    private ItemStack defaultItem;

    Items(ItemStack item) {
        this.item = item;
    }

    Items(ItemStack item, ItemStack defaultItem) {
        this.item = item;
        this.defaultItem = defaultItem;
    }

    public ItemStack getItem() {
        if (item != null) return item;
        else if (defaultItem != null) return defaultItem;
        else return null;
    }
}
