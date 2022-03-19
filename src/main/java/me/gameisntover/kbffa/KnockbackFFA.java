package me.gameisntover.kbffa;

import lombok.Getter;
import me.gameisntover.kbffa.arena.*;
import me.gameisntover.kbffa.listeners.*;
import me.gameisntover.kbffa.hook.Expansion;
import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.customconfig.*;
import me.gameisntover.kbffa.command.ArenaCommands;
import me.gameisntover.kbffa.command.Commands;
import me.gameisntover.kbffa.command.CommandsTabCompleter;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import me.gameisntover.kbffa.scoreboard.MainScoreboard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public final class KnockbackFFA extends JavaPlugin implements Listener {
        
        private int arenaID = 0;
        private Integer timer = 0;
        private BlockDataManager manager;
        private FileConfiguration messages;
        private FileConfiguration sounds;
        @Override
        public void onEnable() {
            manager = BlockDataManager.createAuto(this, this.getDataFolder().toPath().resolve("blocks.db"), true, true);
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                for (Player player : Bukkit.getOnlinePlayers())
                    KnockbackFFAAPI.setInGamePlayer(player, KnockbackFFAAPI.BungeeMode());
            }


            getLogger().info("Loading Commands");
            loadCommands();
            getLogger().info("Loading Configuration Files");
            loadMessages();
            loadSounds();
            loadConfig();
            getLogger().info("Loading Java Classes");
            loadListeners();
            getLogger().info("Loading Tasks");
            loadTasks();
            getLogger().info("Enjoy using plugin :)");

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(p)) return;
                    if (p.getInventory().contains(Material.BOW) && !p.getInventory().contains(Material.ARROW)) {
                        KnockbackFFAKit kitManager = new KnockbackFFAKit();
                        p.getInventory().addItem(kitManager.kbbowArrow());
                    }
            }

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Bukkit.getPluginManager().registerEvents(this, this);
                new Expansion().register();
            } else getLogger().warning("Could not find placeholder API. This plugin is needed!");

        }

        public void loadMessages() {
            File file = new File("plugins/KnockbackFFA/messages.yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    saveResource("messages.yml", true);
                } catch (IOException ignored) {}
            }
            messages = YamlConfiguration.loadConfiguration(file);
        }
        public void loadSounds(){
            File file = new File("plugins/KnockbackFFA/sounds.yml");
            if (!file.exists()){
                try {
                    file.createNewFile();
                    saveResource("sound.yml", true);
                }catch (IOException ignored){}
            }
            sounds = YamlConfiguration.loadConfiguration(file);
        }

        public static KnockbackFFA getInstance() {
            return getPlugin(KnockbackFFA.class);
        }

        private void loadConfig() {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                getLogger().info("Creating DataFolder");
                dataFolder.mkdir();
                File arenaDataFolder = new File(dataFolder+ File.separator + "ArenaData");
                arenaDataFolder.mkdir();
            }
            File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "Kits" + File.separator);
            if (!folder.exists()) {
                folder.mkdir();
                File file = new File("plugins/KnockbackFFA/Kits/Default.yml");
                try {
                    file.createNewFile();
                    Files.copy(Objects.requireNonNull(KnockbackFFA.getInstance().getResource("Default.yml")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    getLogger().info("Default Kit Created");
                } catch (IOException ignored) {}
            }
            CosmeticConfiguration.setup();
            ArenaConfiguration.setup();
            ScoreboardConfiguration.setup();
            ItemConfiguration.setup();
            saveDefaultConfig();

        }

        private void loadTasks() {
            if (Arena.getfolder().listFiles() == null || Arena.getfolder().listFiles().length == 0) return;
                List<Arena> arenaList = Arena.getArenaList();
                Arena.setEnabledArena(arenaList.get(0));
                timer = getConfig().getInt("ArenaChangeTimer");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (arenaList.size() > 0) {
                            String arenaName = arenaList.get(0).getName();
                            Arena.setEnabledArena(arenaName);
                            ArenaConfiguration.save();
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(p)) return;
                                Arena.changeArena(Arena.load(arenaName.replace(".yml","")));
                                cancel();
                            }
                            if (arenaList.size() > 1) arenaID++;

                        }
                    }
                }.runTaskTimer(this, 0, 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        timer--;
                        if (timer == 0) {
                            //what should happen when timer is up
                            timer = getConfig().getInt("ArenaChangeTimer");
                            if (arenaList.size() > 1) { //checking if arenaList even has arenas
                                arenaID++;
                                if (!(arenaID <= arenaList.size())) arenaID = 1;
                                Arena.changeArena(arenaList.get(arenaID - 1));
                            } else if (arenaList.size() == 1) Arena.setEnabledArena(arenaList.get(0).getName());

                        }
                    }
                }.runTaskTimer(this, 0, 20);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(p)) return;
                            World world = p.getWorld();
                            List<Entity> entList = world.getEntities();
                            for (Entity current : entList) if (current instanceof Item) if (((Item) current).getItemStack().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) current.remove();
                    }
                }
            }.runTaskTimer(this, 0, 5);

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            if (!getConfig().getBoolean("ClearItems.enabled")) return;
            scheduler.scheduleSyncRepeatingTask(this, () -> {
            Bukkit.broadcastMessage(Message.ITEM_CLEAR.toString().replace("&", "§"));
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(p)) return;
                    World world = p.getWorld();
                    List<Entity> entList = world.getEntities();
                    for (Entity current : entList) {
                        if (!(current instanceof Item)) return;
                            current.remove();
                            p.playSound(p.getLocation(), Sound.valueOf(Sounds.ITEM_REMOVED.toString()), 1, 1);
                        }
                    }
            }, getConfig().getInt("ClearItems.delay"), getConfig().getInt("ClearItems.period") * 20L);
        }

        private void loadListeners() {
            Arrays.asList(new NoHunger(), new JoinLeaveListeners(), new ChatListener(),
                    new DeathListener(), new WandListener(), new GameRules(), new MainScoreboard(),
                    new GuiStuff(), new KnockbackFFAKit(), new ArenaSettings())
                    .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        }

        private void loadCommands() {
            getCommand(Arrays.asList("join","leave","reload","setmainLobby","createworld"
                    ,"setvoid","createkit","delkit","specialitems","resetarena").toString()).setExecutor(new Commands());
            getCommand(Arrays.asList("wand","setsafezone","gotoworld","createarena","editarena").toString()).setExecutor(new ArenaCommands());
            getCommand("gotoworld").setTabCompleter(new CommandsTabCompleter());
            getCommand("editarena").setTabCompleter(new CommandsTabCompleter());
            getCommand("resetarena").setTabCompleter(new CommandsTabCompleter());
        }

        @EventHandler
        public void onSign(SignChangeEvent event) {
            if ("[KnockbackFFA]".equalsIgnoreCase(event.getLine(0)) && "Join".equalsIgnoreCase(event.getLine(1))) {
                event.setLine(0, ChatColor.YELLOW + "[A]KnockbackFFA");
                event.setLine(1, ChatColor.GREEN + "Join");
            }
        }

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent e) {
            Player player = e.getPlayer();
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                if (e.getBlockPlaced().getType() == Material.WHITE_WOOL) {
                    Block block = e.getBlockPlaced();
                    DataBlock db = manager.getDataBlock(block);
                    db.set("block-type", "BuildingBlock");
                    String arenaName = Arena.getEnabledArena().getName();
                    BukkitRunnable runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (Arena.getEnabledArena().getName().equals(arenaName)) {
                                switch (block.getType()) {
                                    case WHITE_WOOL:
                                        block.setType(Material.YELLOW_WOOL);
                                        break;
                                    case YELLOW_WOOL:
                                        block.setType(Material.ORANGE_WOOL);
                                        break;
                                    case ORANGE_WOOL:
                                        block.setType(Material.RED_WOOL);
                                        break;
                                    case RED_WOOL:
                                        block.setType(Material.AIR);
                                        cancel();
                                        break;
                                }
                            } else {
                                block.setType(Material.AIR);
                                db.set("block-type", "");
                            }
                        }
                    };
                    runnable.runTaskTimer(this, 10L, 20L);
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    int i = scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            KnockbackFFAKit kitManager = new KnockbackFFAKit();
                            player.getInventory().addItem(kitManager.BuildingBlock());
                        }
                    }, 1);
                }
                if (e.getBlockPlaced().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                    Block block = e.getBlockPlaced();
                    block.getDrops().clear();
                    BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
                    blockTimer.scheduleSyncDelayedTask(this,
                            () -> e.getBlock().setType(Material.AIR), 100);
                }
            }
        }

        @EventHandler
        public void onPressureButton(PlayerInteractEvent e) {
            Player player = e.getPlayer();
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                if (e.getAction().equals(Action.PHYSICAL)) {
                    if (e.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                        Block block = e.getClickedBlock();
                        block.getDrops().clear();
                        player.setVelocity(player.getLocation().getDirection().setY(ItemConfiguration.get().getInt("SpecialItems.JumpPlate.jumpLevel")));
                        player.playSound(player.getLocation(), Sound.valueOf(Sounds.JUMP_PLATE.toString()), 1, 1);
                    }
                }
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock().getState() instanceof Sign || e.getClickedBlock().getState() instanceof WallSign) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if (!(ChatColor.YELLOW + "[A]KnockbackFFA").equalsIgnoreCase(sign.getLine(0))) return;
                    if (!(ChatColor.GREEN + "Join").equalsIgnoreCase(sign.getLine(1))) return;
                    if (KnockbackFFAAPI.isInGame(player))
                        player.sendMessage(ChatColor.RED + "You are already in the game!");
                    else player.chat("/join");
                }
            }
        }

}