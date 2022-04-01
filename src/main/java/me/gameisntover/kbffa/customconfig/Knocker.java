package me.gameisntover.kbffa.customconfig;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.With;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.gameevents.GameEvent;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.scoreboard.SideBar;
import me.gameisntover.kbffa.util.Sounds;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.util.List;

@Data
public class Knocker {
    private final File df = KnockbackFFA.getINSTANCE().getDataFolder();
    private File file;
    private Player player;
    private String name;
    private File folder = new File(getDf(), "player data" + File.separator);
    private FileConfiguration config;
    private boolean inGame = KnockbackFFA.getINSTANCE().BungeeMode();
    private boolean inArena;
    private boolean scoreboard;
    private Inventory inventory;
    private int killStreak = 0;
    @SneakyThrows
    public Knocker(Player player) {
        setPlayer(player);
        setFile(new File(getFolder(), player.getUniqueId() + ".yml"));
        if (!df.exists()) df.mkdir();
        if (!file.exists()) file.createNewFile();
        setConfig(YamlConfiguration.loadConfiguration(file));
        setName(player.getDisplayName());
        setInventory(player.getInventory());
    }

    @SneakyThrows
    public void saveConfig() {
        config.save(file);
    }

    public void showScoreBoard() {
        scoreboard = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                SideBar sidebar = new SideBar(ChatColor.translateAlternateColorCodes('&',ScoreboardConfiguration.get().getString("Title")), "mainScoreboard");
                if (!scoreboard) {
                    cancel();
                sidebar.getBoard().clearSlot(DisplaySlot.SIDEBAR);
                }
                List<String> scoreboardLines = ScoreboardConfiguration.get().getStringList("lines");
                for (String string : scoreboardLines) {
                    string = PlaceholderAPI.setPlaceholders(player,string);
                    sidebar.add(ChatColor.translateAlternateColorCodes('&',string));
                }
                    applySideBar(sidebar);
            }
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, 20);
    }
    public void applySideBar(SideBar sideBar){
        for (int i = 0; i < sideBar.getScores().size(); i++)
            sideBar.getScores().get(i).setScore(i);
        player.setScoreboard(sideBar.getBoard());
    }
    public void hideScoreBoard() {
        scoreboard = false;
    }

    public String selectedCosmetic() {
        return getConfig().getString("selected-cosmetic");
    }

    public String selectedKit() {
        return getConfig().getString("selected-kit");
    }

    public void loadCosmetic(String cosmeticName) {
        if (cosmeticName == null) return;
        String cosmeticType = CosmeticConfiguration.get().getString(cosmeticName + ".type");
        if (cosmeticType == null) return;
        if (cosmeticType.equalsIgnoreCase("KILL_PARTICLE"))
            player.spawnParticle(Particle.valueOf(CosmeticConfiguration.get().getString(cosmeticName + ".effect-type")), player.getLocation(), CosmeticConfiguration.get().getInt(cosmeticName + ".amount"));
        if (CosmeticConfiguration.get().getString(CosmeticConfiguration.get().getString(cosmeticName + ".sound")) != null)
            player.playSound(player.getLocation(), Sound.valueOf(CosmeticConfiguration.get().getString(cosmeticName + ".sound")), CosmeticConfiguration.get().getInt(cosmeticName + ".volume"), CosmeticConfiguration.get().getInt(cosmeticName + ".pitch"));
        if (cosmeticType.equalsIgnoreCase("SOUND")) {
            List<String> soundList = CosmeticConfiguration.get().getStringList(cosmeticName + ".sounds");
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < soundList.size(); i++) {
                        String sound = soundList.get(i).substring(0, soundList.get(i).indexOf(":"));
                        float pitch = Float.parseFloat(soundList.get(i).substring(soundList.get(i).indexOf(":") + 1, soundList.get(i).indexOf(",")));
                        float volume = Float.parseFloat(soundList.get(i).substring(soundList.get(i).indexOf(",") + 1));
                        player.getWorld().playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
                        if (i == soundList.size() - 1) cancel();
                    }
                }
            }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, CosmeticConfiguration.get().getInt(cosmeticName + ".delay"));
        }
    }
    public void playSound(Sounds sound){
        getPlayer().playSound(getPlayer().getLocation(),Sound.valueOf(sound.toString()),1,1);
    }
    public void openGUI(GUI gui){
        getPlayer().openInventory(gui.getInventory());
    }
    public void sendMessage(String message){
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',message));
    }
    public void sendActionBar(String message){
        getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }
}