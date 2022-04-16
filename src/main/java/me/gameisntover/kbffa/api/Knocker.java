package me.gameisntover.kbffa.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.scoreboard.SideBar;
import me.gameisntover.kbffa.util.Items;
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

@EqualsAndHashCode(callSuper = true)
@Data
public class Knocker extends KnockData {
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
    private double balance;
    private int killStreak = 0;

    @SneakyThrows
    public Knocker(Player player) {
        this.player = player;
        this.file = loadDataFile(folder, player.getUniqueId() + ".yml");
        if (!df.exists()) df.mkdir();
        if (!file.exists()) file.createNewFile();
        this.config = YamlConfiguration.loadConfiguration(file);
        this.name = player.getName();
        this.inventory = player.getInventory();
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
                SideBar sidebar = new SideBar(ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getKnockScoreboard().getConfig.getString("Title")), "mainScoreboard");
                if (!scoreboard) {
                    cancel();
                    sidebar.getBoard().clearSlot(DisplaySlot.SIDEBAR);
                }
                List<String> scoreboardLines = KnockbackFFA.getINSTANCE().getKnockScoreboard().getConfig.getStringList("lines");
                for (String string : scoreboardLines) {
                    string = PlaceholderAPI.setPlaceholders(player, string);
                    sidebar.add(ChatColor.translateAlternateColorCodes('&', string));
                }
                applySideBar(sidebar);
            }
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, 20);
    }

    public void applySideBar(SideBar sideBar) {
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
        String cosmeticType = KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".type");
        if (cosmeticType == null) return;
        if (cosmeticType.equalsIgnoreCase("KILL_PARTICLE"))
            player.spawnParticle(Particle.valueOf(KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".effect-type")), player.getLocation(), KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".amount"));
        if (KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".sound")) != null)
            player.playSound(player.getLocation(), Sound.valueOf(KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".sound")), KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".volume"), KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".pitch"));
        if (cosmeticType.equalsIgnoreCase("SOUND")) {
            List<String> soundList = KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getStringList(cosmeticName + ".sounds");
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
            }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".delay"));
        }
    }

    public void playSound(Sounds sound) {
        getPlayer().playSound(getPlayer().getLocation(), Sound.valueOf(sound.toString()), 1, 1);
    }

    public void openGUI(GUI gui) {
        getPlayer().openInventory(gui.getInventory());
    }

    public void sendMessage(String message) {
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendActionBar(String message) {
        getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void setBalance(double balance) {
        getConfig().set("balance", balance);
        saveConfig();
    }

    public float getBalance() {
        return getConfig().getInt("balance");
    }

    public void addBalance(float balance) {
        setBalance(getBalance() + balance);
    }

    public void removeBalance(int balance) {
        setBalance(getBalance() - balance);
    }

    public void removeBalance(double balance) {
        setBalance(getBalance() - balance);
    }

    public void giveLobbyItems() {
        getPlayer().getInventory().setItem(KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("LobbyItems.shop.slot"), Items.SHOP_ITEM.getItem());
        getPlayer().getInventory().setItem(KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("LobbyItems.cosmetic.slot"), Items.COSMETIC_ITEM.getItem());
        getPlayer().getInventory().setItem(KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("LobbyItems.kits.slot"), Items.KIT_ITEM.getItem());

    }
}