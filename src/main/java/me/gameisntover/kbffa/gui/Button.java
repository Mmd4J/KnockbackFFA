package me.gameisntover.kbffa.gui;

import lombok.Data;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Data
public abstract class Button {
    protected ItemStack item;
    private ItemMeta meta;
    private Inventory inventory;
    protected int slot;
    public Button(ItemStack item) {
        setItem(item);
        setMeta(item.getItemMeta());
        setSelected(false);
    }
    public void setSelected(boolean toggle){
        if (toggle) meta.addEnchant(Enchantment.DURABILITY,1,true);
        else meta.removeEnchant(Enchantment.DURABILITY);
        item.setItemMeta(meta);
    }
    public abstract void onClick(InventoryClickEvent e);
}