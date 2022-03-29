package me.gameisntover.kbffa.customconfig;

import lombok.Data;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.scoreboard.SideBar;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
    private File folder = new File(getDf(), "players" + File.separator);
    private FileConfiguration config;
    private boolean inGame = KnockbackFFA.getINSTANCE().BungeeMode();
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
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, 20);
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

}