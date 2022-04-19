package me.gameisntover.kbffa.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.util.Items;
import me.gameisntover.kbffa.util.Sounds;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Knocker extends KnockData {
    private final File df = KnockbackFFA.getInstance().getDataFolder();
    private File file;
    private UUID uuid;
    private File folder = new File(getDf(), "player data" + File.separator);
    private FileConfiguration config;
    private boolean inGame = KnockbackFFA.getInstance().BungeeMode();
    private boolean inArena;
    private boolean scoreboard;
    private double balance;
    private int killStreak = 0;
    private Inventory inventory;

    @SneakyThrows
    public Knocker(UUID uuid) {
        this.uuid = uuid;
        this.file = loadDataFile(folder, uuid + ".yml");
        if (!df.exists()) df.mkdir();
        if (!file.exists()) file.createNewFile();
        this.config = YamlConfiguration.loadConfiguration(file);
        this.inventory = getPlayer().getInventory();
    }

    @SneakyThrows
    public void saveConfig() {
        config.save(file);
    }

    public void showScoreBoard() {
        if(!scoreboard) scoreboard = true;
        KnockbackFFA.getInstance().getScoreboardManager().register(getPlayer());
    }

    public void hideScoreBoard() {
        scoreboard = false;
        KnockbackFFA.getInstance().getScoreboardManager().unregister(getPlayer());
    }

    public String selectedCosmetic() {
        return getConfig().getString("selected-cosmetic");
    }

    public String selectedKit() {
        return getConfig().getString("selected-kit");
    }

    public void loadCosmetic(String cosmeticName) {
        if (cosmeticName == null) return;
        String cosmeticType = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".type");
        if (cosmeticType == null) return;
        if (cosmeticType.equalsIgnoreCase("KILL_PARTICLE"))
            getPlayer().spawnParticle(Particle.valueOf(KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".effect-type")), getPlayer().getLocation(), KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".amount"));
        if (KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".sound")) != null)
            getPlayer().playSound(getPlayer().getLocation(), Sound.valueOf(KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmeticName + ".sound")), KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".volume"), KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".pitch"));
        if (cosmeticType.equalsIgnoreCase("SOUND")) {
            List<String> soundList = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getStringList(cosmeticName + ".sounds");
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < soundList.size(); i++) {
                        String sound = soundList.get(i).substring(0, soundList.get(i).indexOf(":"));
                        float pitch = Float.parseFloat(soundList.get(i).substring(soundList.get(i).indexOf(":") + 1, soundList.get(i).indexOf(",")));
                        float volume = Float.parseFloat(soundList.get(i).substring(soundList.get(i).indexOf(",") + 1));
                        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.valueOf(sound), volume, pitch);
                        if (i == soundList.size() - 1) cancel();
                    }
                }
            }.runTaskTimer(KnockbackFFA.getInstance(), 0, KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(cosmeticName + ".delay"));
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

    public float getBalance() {
        return getConfig().getInt("balance");
    }

    public void setBalance(double balance) {
        getConfig().set("balance", balance);
        saveConfig();
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
        getPlayer().getInventory().setItem(KnockbackFFA.getInstance().getItems().getConfig.getInt("LobbyItems.shop.slot"), Items.SHOP_ITEM.getItem());
        getPlayer().getInventory().setItem(KnockbackFFA.getInstance().getItems().getConfig.getInt("LobbyItems.cosmetic.slot"), Items.COSMETIC_ITEM.getItem());
        getPlayer().getInventory().setItem(KnockbackFFA.getInstance().getItems().getConfig.getInt("LobbyItems.kits.slot"), Items.KIT_ITEM.getItem());

    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}