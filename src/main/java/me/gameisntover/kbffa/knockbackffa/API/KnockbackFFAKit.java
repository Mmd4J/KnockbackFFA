package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ItemConfiguration;
import me.gameisntover.kbffa.knockbackffa.MaterialLegacy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

public class KnockbackFFAKit implements Listener
{

    public void DefaultKit(OfflinePlayer player) {
        Player player1 = player.getPlayer();
        if (KnockbackFFAAPI.isLegacyVersion()) {
            ItemStack kbstick = new ItemStack(Material.LEGACY_STICK, 1);
            ItemMeta meta = kbstick.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Knocbkack Stick");
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            kbstick.setItemMeta(meta);
            ItemStack enderpearl = new ItemStack(Material.LEGACY_ENDER_PEARL, 16);
            ItemMeta enderpearlmeta = enderpearl.getItemMeta();
            enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
            enderpearl.setItemMeta(enderpearlmeta);
            ItemStack jumpPlate = new ItemStack(Material.LEGACY_GOLD_PLATE, 3);
            ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
            jumpPlateMeta.setDisplayName(ChatColor.YELLOW + "Jump Plate");
            jumpPlate.setItemMeta(jumpPlateMeta);
            ItemStack buildingBlock = new ItemStack(Material.LEGACY_WHITE_GLAZED_TERRACOTTA, 64);
            ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
            buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
            buildingBlock.setItemMeta(buildingBlockMeta);
            ItemStack kbBow = new ItemStack(Material.LEGACY_BOW, 1);
            ItemMeta kbBowMeta = kbBow.getItemMeta();
            kbBowMeta.setDisplayName(ChatColor.AQUA + "Knockback Bow");
            kbBowMeta.setUnbreakable(true);
            kbBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 3, true);
            kbBow.setItemMeta(kbBowMeta);
            ItemStack kbArrow = new ItemStack(Material.LEGACY_ARROW, 1);
            ItemMeta kbArrowMeta = kbArrow.getItemMeta();
            kbArrowMeta.setDisplayName(ChatColor.AQUA + "Knockback Arrow");
            kbArrow.setItemMeta(kbArrowMeta);
            Inventory pinventory = player1.getInventory();
            pinventory.clear();
            pinventory.addItem(kbstick, enderpearl, jumpPlate, buildingBlock, kbBow, kbArrow);

        } else {
            ItemStack kbstick = new ItemStack(Material.STICK, 1);
            ItemMeta meta = kbstick.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Knocbkack Stick");
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            kbstick.setItemMeta(meta);
            ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
            ItemMeta enderpearlmeta = enderpearl.getItemMeta();
            enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
            enderpearl.setItemMeta(enderpearlmeta);
            ItemStack jumpPlate = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 3);
            ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
            jumpPlateMeta.setDisplayName(ChatColor.YELLOW + "Jump Plate");
            jumpPlate.setItemMeta(jumpPlateMeta);
            ItemStack buildingBlock = new ItemStack(Material.WHITE_WOOL, 64);
            ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
            buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
            buildingBlock.setItemMeta(buildingBlockMeta);
            ItemStack kbBow = new ItemStack(Material.BOW, 1);
            ItemMeta kbBowMeta = kbBow.getItemMeta();
            kbBowMeta.setDisplayName(ChatColor.AQUA + "Knockback Bow");
            kbBowMeta.setUnbreakable(true);
            kbBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 3, true);
            kbBow.setItemMeta(kbBowMeta);
            ItemStack kbArrow = new ItemStack(Material.ARROW, 1);
            ItemMeta kbArrowMeta = kbArrow.getItemMeta();
            kbArrowMeta.setDisplayName(ChatColor.AQUA + "Knockback Arrow");
            kbArrow.setItemMeta(kbArrowMeta);
            Inventory pinventory = player1.getInventory();
            pinventory.clear();
            pinventory.addItem(kbstick, enderpearl, jumpPlate, buildingBlock, kbBow, kbArrow);
        }
    }

    public void kbbowArrow(OfflinePlayer player, Integer amount) {
        if (KnockbackFFAAPI.isLegacyVersion()) {
            Player player1 = player.getPlayer();
            ItemStack kbArrow = new ItemStack(Material.getMaterial(MaterialLegacy.ARROW.name()), amount);
            ItemMeta kbArrowMeta = kbArrow.getItemMeta();
            kbArrowMeta.setDisplayName(ChatColor.AQUA + "Knockback Arrow");
            kbArrow.setItemMeta(kbArrowMeta);
            Inventory pinventory = player1.getInventory();
            pinventory.addItem(kbArrow);
        } else {
            Player player1 = player.getPlayer();
            ItemStack kbArrow = new ItemStack(Material.ARROW, amount);
            ItemMeta kbArrowMeta = kbArrow.getItemMeta();
            kbArrowMeta.setDisplayName(ChatColor.AQUA + "Knockback Arrow");
            kbArrow.setItemMeta(kbArrowMeta);
            Inventory pinventory = player1.getInventory();
            pinventory.addItem(kbArrow);
        }
    }

    public void BuildingBlock(OfflinePlayer player, Integer amount) {
        Player player1 = player.getPlayer();
        Inventory pinventory = player1.getInventory();
        if (KnockbackFFAAPI.isLegacyVersion()) {
            ItemStack buildingBlock = new ItemStack(Material.LEGACY_WHITE_GLAZED_TERRACOTTA, amount);
            ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
            buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
            buildingBlock.setItemMeta(buildingBlockMeta);
            pinventory.addItem(buildingBlock);
        } else {
            ItemStack buildingBlock = new ItemStack(Material.WHITE_WOOL, amount);
            ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
            buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
            buildingBlock.setItemMeta(buildingBlockMeta);
            pinventory.addItem(buildingBlock);
        }
    }

    public void JumpPlate(OfflinePlayer player, Integer amount) {
        Player player1 = player.getPlayer();
        Inventory pinventory = player1.getInventory();
        if (KnockbackFFAAPI.isLegacyVersion()) {
            ItemStack jumpPlate = new ItemStack(Material.LEGACY_GOLD_PLATE, amount);
            ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
            jumpPlateMeta.setDisplayName(ChatColor.YELLOW + "Jump Plate");
            jumpPlate.setItemMeta(jumpPlateMeta);
            pinventory.addItem(jumpPlate);
        } else {
            ItemStack jumpPlate = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, amount);
            ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
            jumpPlateMeta.setDisplayName(ChatColor.YELLOW + "Jump Plate");
            jumpPlate.setItemMeta(jumpPlateMeta);
            pinventory.addItem(jumpPlate);
        }
    }

    public void EnderPearl(OfflinePlayer player, Integer amount) {
        Player player1 = player.getPlayer();
        Inventory pinventory = player1.getInventory();
        if (KnockbackFFAAPI.isLegacyVersion()) {
            ItemStack enderpearl = new ItemStack(Material.LEGACY_ENDER_PEARL, amount);
            ItemMeta enderpearlmeta = enderpearl.getItemMeta();
            enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
            enderpearl.setItemMeta(enderpearlmeta);
            pinventory.addItem(enderpearl);
        } else {
            ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, amount);
            ItemMeta enderpearlmeta = enderpearl.getItemMeta();
            enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
            enderpearl.setItemMeta(enderpearlmeta);
            pinventory.addItem(enderpearl);
        }
    }
    public void lobbyItems(Integer shopSlot, Integer cosmeticSlot, Integer kitsSlot, Player player){
        ItemStack shop = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.shop.material")));
        ItemStack cosmetics = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.cosmetic.material")));
        ItemStack kits = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.kits.material")));
        ItemMeta shopMeta = shop.getItemMeta();
        ItemMeta cosmeticsMeta = cosmetics.getItemMeta();
        ItemMeta kitsMeta = kits.getItemMeta();
        shopMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.shop.name").replace("&", "§"));
        cosmeticsMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.cosmetic.name").replace("&", "§"));
        kitsMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.kits.name").replace("&", "§"));
        List<String> shopLore = ItemConfiguration.get().getStringList("LobbyItems.shop.lore");
        List<String> cosmeticsLore = ItemConfiguration.get().getStringList("LobbyItems.cosmetic.lore");
        List<String> kitsLore = ItemConfiguration.get().getStringList("LobbyItems.kits.lore");
        shopMeta.setLore(shopLore);
        cosmeticsMeta.setLore(cosmeticsLore);
        kitsMeta.setLore(kitsLore);
        shopMeta.setUnbreakable(true);
        cosmeticsMeta.setUnbreakable(true);
        kitsMeta.setUnbreakable(true);
        kitsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        cosmeticsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        shopMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        kitsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        cosmeticsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shop.setItemMeta(shopMeta);
        cosmetics.setItemMeta(cosmeticsMeta);
        kits.setItemMeta(kitsMeta);
        Inventory pinventory = player.getInventory();
        pinventory.setItem(shopSlot, shop);
        pinventory.setItem(cosmeticSlot, cosmetics);
        pinventory.setItem(kitsSlot, kits);
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
