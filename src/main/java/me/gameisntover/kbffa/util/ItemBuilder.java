package me.gameisntover.kbffa.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
public class ItemBuilder {
    private Material material;
    private int amount;
    private String name;
    private List<String> lore;
    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material, int amount, String name, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
    }

    public ItemBuilder(Material material, int amount, String name) {
        this.material = material;
        this.amount = amount;
        this.name = name;
    }

    public ItemStack create() {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack create(Enchantment enchantment, int level) {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(enchantment, level, true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack create(ItemFlag flag, ItemFlag flag2, Enchantment enchant, int level) {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(flag, flag2);
        if (flag2.equals(ItemFlag.HIDE_UNBREAKABLE)) meta.setUnbreakable(true);
        meta.addEnchant(enchant, level, true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack create(Enchantment enchantment, int level, ItemFlag itemFlag) {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(enchantment, level, true);
        meta.addItemFlags(itemFlag);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack create(ItemFlag itemFlag) {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(itemFlag);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void addEnchant(Enchantment enchant, int level) {
        meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);
    }

    public void addItemFlag(ItemFlag itemFlag) {
        meta.addItemFlags(itemFlag);
        item.setItemMeta(meta);
    }
}
