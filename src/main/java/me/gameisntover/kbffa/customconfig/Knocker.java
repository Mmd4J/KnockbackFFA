package me.gameisntover.kbffa.customconfig;

import lombok.Data;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.scoreboard.SideBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.util.List;

@Data
public class Knocker {
    private final File df = KnockbackFFA.getInstance().getDataFolder();
    private File file;
    private Player player;
    private String name;
    private File folder = new File(getDf(), "players" + File.separator);
    private FileConfiguration config;
    private boolean inGame = KnockbackFFA.getInstance().getAPI().BungeeMode();
    private boolean inArena;
    private boolean scoreboard;

    @SneakyThrows
    public Knocker(Player player) {
        setPlayer(player);
        setFile(new File(df, player.getUniqueId() + ".yml"));
        if (!df.exists()) df.mkdir();
        if (!file.exists()) file.createNewFile();
        setConfig(YamlConfiguration.loadConfiguration(file));
        setName(player.getDisplayName());
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
                List<String> scoreboardLines = ScoreboardConfiguration.get().getStringList("lines");
                SideBar sidebar = new SideBar(ScoreboardConfiguration.get().getString("Title").replace("&", "§"), "mainScoreboard");
                player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                for (String string : scoreboardLines) {
                    string = PlaceholderAPI.setPlaceholders(player, string);
                    sidebar.add(string.replaceAll("&", "§"));
                }
                if (!scoreboard) {
                    cancel();
                    player.getScoreboard().getObjectives().clear();
                }
                sidebar.apply(player);
            }
        }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);
    }

    public void hideScoreBoard() {
        scoreboard = false;
    }
}