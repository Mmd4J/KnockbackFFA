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
import java.util.stream.Collectors;

public class KnockbackFFAKit implements Listener
{

    public void DefaultKit(OfflinePlayer player) {
        Player player1 = player.getPlayer();
        if (KnockbackFFAAPI.isLegacyVersion()) {
            ItemStack kbstick = new ItemStack(Material.getMaterial(MaterialLegacy.STICK.name()),1);
            ItemMeta meta = kbstick.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Knocbkack Stick");
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            kbstick.setItemMeta(meta);
            ItemStack enderpearl = new ItemStack(Material.getMaterial(MaterialLegacy.ENDER_PEARL.name()), 16);
            ItemMeta enderpearlmeta = enderpearl.getItemMeta();
            enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
            enderpearl.setItemMeta(enderpearlmeta);
            ItemStack jumpPlate = new ItemStack(Material.getMaterial(MaterialLegacy.GOLD_PLATE.name()), 3);
            ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
            jumpPlateMeta.setDisplayName(ChatColor.YELLOW + "Jump Plate");
            jumpPlate.setItemMeta(jumpPlateMeta);
            ItemStack buildingBlock = new ItemStack(Material.getMaterial(MaterialLegacy.IRON_BLOCK.name()), 64);
            ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
            buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
            buildingBlock.setItemMeta(buildingBlockMeta);
            ItemStack kbBow = new ItemStack(Material.getMaterial(MaterialLegacy.BOW.name()), 1);
            ItemMeta kbBowMeta = kbBow.getItemMeta();
            kbBowMeta.setDisplayName(ChatColor.AQUA + "Knockback Bow");
            kbBowMeta.setUnbreakable(true);
            kbBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 3, true);
            kbBow.setItemMeta(kbBowMeta);
            ItemStack kbArrow = new ItemStack(Material.getMaterial(MaterialLegacy.ARROW.name()), 1);
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
            ItemStack buildingBlock = new ItemStack(Material.getMaterial(MaterialLegacy.IRON_BLOCK.name()), amount);
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
            ItemStack jumpPlate = new ItemStack(Material.getMaterial(MaterialLegacy.GOLD_PLATE.name()), amount);
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
            ItemStack enderpearl = new ItemStack(Material.getMaterial(MaterialLegacy.ENDER_PEARL.name()), amount);
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
    public ItemStack cosmetic() {
         ItemStack cosmetics = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.cosmetic.material")));
        return cosmetics;
    }
    public ItemMeta guiItemMeta(ItemStack guiItem){
        ItemMeta guimeta = guiItem.getItemMeta();
        guimeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        guimeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        guimeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        guimeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        guimeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        guimeta.setUnbreakable(true);
        return guimeta;
    }
    public ItemMeta cosmeticMeta(){
     ItemMeta cosmeticsMeta = guiItemMeta(cosmetic());
        cosmeticsMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.cosmetic.name").replace("&", "§"));
        List<String> cosmeticsLore = ItemConfiguration.get().getStringList("LobbyItems.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
        cosmeticsMeta.setLore(cosmeticsLore);
        return cosmeticsMeta;
    }
    public ItemStack kits(){
        ItemStack kit = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.kits.material")));
        return kit;
    }
    public ItemMeta kitsMeta(){
        ItemMeta kitMeta = guiItemMeta(kits());
        kitMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.kits.name").replace("&", "§"));
        List<String> kitsLore = ItemConfiguration.get().getStringList("LobbyItems.kits.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
        kitMeta.setLore(kitsLore);
        return kitMeta;
    }
    public ItemStack shop(){
        ItemStack shop = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.shop.material")));
        return shop;
    }
    public ItemMeta shopMeta(){
        ItemMeta shopMeta = guiItemMeta(shop());
        shopMeta.setDisplayName(ItemConfiguration.get().getString("LobbyItems.shop.name").replace("&", "§"));
        List<String> shopLore = ItemConfiguration.get().getStringList("LobbyItems.shop.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
        shopMeta.setLore(shopLore);
        return shopMeta;
    }
    public void lobbyItems(Integer shopSlot, Integer cosmeticSlot, Integer kitsSlot, Player player){
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
        pinventory.setItem(shopSlot, shop);
        pinventory.setItem(cosmeticSlot, cosmeticMenu);
        pinventory.setItem(kitsSlot, kitsMenu);
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
