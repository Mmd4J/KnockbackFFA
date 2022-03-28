package me.gameisntover.kbffa.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private final Material material;
    private final int amount;
    private final String name;
    private final List<String> lore;


    public ItemStack create(){
        item = new ItemStack(material,amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack create(Enchantment enchantment, int level){
        item = new ItemStack(material,amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(enchantment,level,true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack create(Enchantment enchantment,int level,ItemFlag itemFlag){
        item = new ItemStack(material,amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(enchantment,level,true);
        meta.addItemFlags(itemFlag);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack create(ItemFlag itemFlag){
        item = new ItemStack(material,amount);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(itemFlag);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public void addEnchant(Enchantment enchant, int level){
        meta.addEnchant(enchant,level,true);
        item.setItemMeta(meta);
    }
    public void addItemFlag(ItemFlag itemFlag){
        meta.addItemFlags(itemFlag);
        item.setItemMeta(meta);
    }
}
