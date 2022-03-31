package me.gameisntover.kbffa.gui;

import lombok.Data;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Data
public class GUI implements Listener {
    private Inventory inventory;
    private String name;
    private short layers;
    private Map<Integer, Button> buttons = new HashMap<>();
    private Consumer<InventoryOpenEvent> openEventConsumer ;
    private Consumer<InventoryCloseEvent> closeEventConsumer;

    public GUI(String name, short guiLayers) {
        setInventory(Bukkit.createInventory(null, guiLayers * 9, name));
        setName(name);
        setLayers(guiLayers);
        Bukkit.getPluginManager().registerEvents(this, KnockbackFFA.getINSTANCE());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
            Button button = buttons.get(e.getSlot());
            if (button == null) return;
            button.onClick(e);
        }
    }

    @EventHandler
    public void onInventoryItemMove(InventoryMoveItemEvent e) {
        if (e.getSource() == inventory || e.getDestination() == inventory || e.getInitiator() == inventory)
            e.setCancelled(true);
    }

    public void add(Button button) {
        getInventory().addItem(button.getItem());
        buttons.put(button.getSlot(), button);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().equals(inventory)) openEventConsumer.accept(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(inventory)) closeEventConsumer.accept(e);
    }

    public void add(Button button, int slot) {
        getInventory().setItem(slot, button.getItem());
        buttons.put(slot, button);
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

}
