package me.gameisntover.kbffa.api;

import me.gameisntover.kbffa.customconfigs.ItemConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class KnockbackFFAKit implements Listener {
    public ItemStack kbStick() {
        ItemStack kbstick = new ItemStack(Material.STICK, 1);
        ItemMeta meta = kbstick.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("SpecialItems.KBStick.name")));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.KNOCKBACK, ItemConfiguration.get().getInt("SpecialItems.KBStick.kb-level"), true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        kbstick.setItemMeta(meta);
        return kbstick;
    }

    public ItemStack kbBow() {
        ItemStack kbBow = new ItemStack(Material.BOW, 1);
        ItemMeta kbBowMeta = kbBow.getItemMeta();
        kbBowMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("SpecialItems.KBBow.name")));
        kbBowMeta.setUnbreakable(true);
        kbBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, ItemConfiguration.get().getInt("SpecialItems.KBBow.kb-level"), true);
        kbBowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        kbBowMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        kbBow.setItemMeta(kbBowMeta);
        return kbBow;
    }

    public ItemStack kbbowArrow() {
        ItemStack kbArrow = new ItemStack(Material.ARROW);
        ItemMeta kbArrowMeta = kbArrow.getItemMeta();
        kbArrowMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("SpecialItems.KBArrow.name")));
        kbArrow.setItemMeta(kbArrowMeta);
        return kbArrow;
    }

    public ItemStack BuildingBlock() {
        ItemStack buildingBlock = new ItemStack(Material.WHITE_WOOL);
        ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
        buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
        buildingBlock.setItemMeta(buildingBlockMeta);
        return buildingBlock;
    }


    public ItemStack JumpPlate() {
        ItemStack jumpPlate = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
        jumpPlateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("SpecialItems.JumpPlate.name")));
        jumpPlate.setItemMeta(jumpPlateMeta);
        return jumpPlate;
    }

    public ItemStack EnderPearl() {
        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta enderpearlmeta = enderpearl.getItemMeta();
        enderpearlmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("SpecialItems.EnderPearl.name")));
        enderpearl.setItemMeta(enderpearlmeta);
        return enderpearl;
    }

    public ItemStack cosmetic() {
        ItemStack cosmetics = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.cosmetic.material")));
        return cosmetics;
    }

    public ItemMeta guiItemMeta(ItemStack guiItem) {
        ItemMeta guimeta = guiItem.getItemMeta();
        guimeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        guimeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        guimeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        guimeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        guimeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        guimeta.setUnbreakable(true);
        return guimeta;
    }

    public ItemMeta cosmeticMeta() {
        ItemMeta cosmeticsMeta = guiItemMeta(cosmetic());
        cosmeticsMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.cosmetic.name").replace("&", "§"));
        List<String> cosmeticsLore = ItemConfiguration.get().getStringList("LobbyItems.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
        cosmeticsMeta.setLore(cosmeticsLore);
        return cosmeticsMeta;
    }

    public ItemStack kits() {
        ItemStack kit = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.kits.material")));
        return kit;
    }

    public ItemMeta kitsMeta() {
        ItemMeta kitMeta = guiItemMeta(kits());
        kitMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.kits.name").replace("&", "§"));
        List<String> kitsLore = ItemConfiguration.get().getStringList("LobbyItems.kits.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
        kitMeta.setLore(kitsLore);
        return kitMeta;
    }

    public ItemStack shop() {
        ItemStack shop = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.shop.material")));
        return shop;
    }

    public ItemMeta shopMeta() {
        ItemMeta shopMeta = guiItemMeta(shop());
        shopMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.shop.name").replace("&", "§"));
        List<String> shopLore = ItemConfiguration.get().getStringList("LobbyItems.shop.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
        shopMeta.setLore(shopLore);
        return shopMeta;
    }

    public void lobbyItems(Player player) {
        Inventory pinventory = player.getInventory();
        ItemStack cosmeticMenu = cosmetic();
        ItemMeta cosmeticMeta = cosmeticMeta();
        ItemStack kitsMenu = kits();
        ItemMeta kitsMeta = kitsMeta();
        ItemStack shop = shop();
        ItemMeta shopMeta = shopMeta();
        cosmeticMenu.setItemMeta(cosmeticMeta);
        kitsMenu.setItemMeta(kitsMeta);
        shop.setItemMeta(shopMeta);
        pinventory.setItem(ItemConfiguration.get().getInt("LobbyItems.shop.slot"), shop);
        pinventory.setItem(ItemConfiguration.get().getInt("LobbyItems.cosmetic.slot"), cosmeticMenu);
        pinventory.setItem(ItemConfiguration.get().getInt("LobbyItems.kits.slot"), kitsMenu);
    }

    @EventHandler
    public void onEndermiteSpawn(CreatureSpawnEvent e) {
        Entity endermite = e.getEntity();
        if (endermite instanceof Endermite) {
            if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.ENDER_PEARL) {
                e.setCancelled(true);
            }
        }
    }

}
