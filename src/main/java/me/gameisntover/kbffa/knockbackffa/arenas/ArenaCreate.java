package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class ArenaCreate  {
    /*  @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (KnockbackFFA.getInstance().getCommand("arena").getLabel().equalsIgnoreCase(command.getName())) {
                Inventory arenaGUI = Bukkit.createInventory(null, 9, "ArenaManager");
                Player player = (Player) sender;
                player.openInventory(arenaGUI);
                ItemStack create = new ItemStack(Material.IRON_AXE, 1);
                ItemMeta meta = create.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "CreateArena");
                meta.setUnbreakable(true);
                create.setItemMeta(meta);
                arenaGUI.addItem(create);
            }
        }
    return false;
    }
    @EventHandler
    public void onGUIClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("ArenaManager")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
        }

    }*/
}

