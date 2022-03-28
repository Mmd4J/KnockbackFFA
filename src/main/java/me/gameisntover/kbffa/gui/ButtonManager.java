package me.gameisntover.kbffa.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ButtonManager{
    public Button create(ItemStack item, BiConsumer<InventoryClickEvent, Button> listener){
        return new Button(item){
            @Override
            public void onClick(InventoryClickEvent e){
                listener.accept(e,this);
            }
        };
    }

    public Button create(ItemStack item, Consumer<InventoryClickEvent> listener) {
        return new Button(item) {
            @Override
            public void onClick(InventoryClickEvent e) {
                listener.accept(e);
            }

        };
    }
}
