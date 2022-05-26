package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.listeners.WandListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetVoidCommand extends KnockCommand {
    public SetVoidCommand() {
        super("setvoid");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "sets a damage zone which must be selected with wand before using the command!";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/setvoid";
    }


    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (WandListener.pos1m.get(p) != null && WandListener.pos2m.get(p) != null) {
            Location pos1 = WandListener.pos1m.get(p);
            Location pos2 = WandListener.pos2m.get(p);
            int vd;
            List<String> voids = KnockbackFFA.getInstance().getZoneConfiguration().getConfig.getStringList("registered-voids");
            if (voids.size() == 0) vd = 1;
            else {
                String szstring = voids.get(voids.size() - 1);
                vd = Integer.parseInt(szstring);
                vd++;
            }
            voids.add(vd + "");
            if (KnockbackFFA.getInstance().getZoneConfiguration().getConfig.getString("voids." + vd) == null) {
                KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("voids." + vd + ".pos1", pos1);
                KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("voids." + vd + ".pos2", pos2);
                KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("voids." + vd + ".damage", 8);
                KnockbackFFA.getInstance().getZoneConfiguration().getConfig.set("registered-voids", voids);
                KnockbackFFA.getInstance().getZoneConfiguration().save();
                p.sendMessage(ChatColor.GREEN + "Void " + vd + " has been set and now players will get damage if they go there");
            }
        } else p.sendMessage(ChatColor.RED + "You have to set two positions first!");

    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
