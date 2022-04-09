package me.gameisntover.kbffa.command.subcommands.kit;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.kit.Kit;
import me.gameisntover.kbffa.api.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CreateKitCommand extends KnockCommand {
    public CreateKitCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getSubName() {
        return "createkit";
    }

    @Override
    public @NotNull String getSubDescription() {
        return ChatColor.AQUA + "Creates kit with the specified name";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/createkit <kitname>";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length == 0){
            p.sendMessage(ChatColor.RED + "Usage: /createkit <kitname>");
        return;
        }
        if (args.length == 1) {
            Kit kit = KnockbackFFA.getINSTANCE().getKitManager().create(args[0],p.getInventory().getContents(),p.getInventory().getItemInMainHand().getType());
            p.sendMessage(ChatColor.GREEN + "I've created the kit " + args[0] + "! now you need to configure it in the plugins plugins/KnockbackFFA/kits!");
        }
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
