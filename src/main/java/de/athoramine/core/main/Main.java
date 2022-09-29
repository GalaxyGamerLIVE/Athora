package de.athoramine.core.main;

import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginLogger;
import de.athoramine.core.commands.AthoraCoreCommand;
import de.athoramine.core.commands.BankCommand;
import de.athoramine.core.commands.BuildCommand;
import de.athoramine.core.commands.DailyCommand;
import de.athoramine.core.commands.FarmSetupCommand;
import de.athoramine.core.commands.GehaltCommand;
import de.athoramine.core.commands.GeneralDestroyableBlocksSetupCommand;
import de.athoramine.core.commands.GiveMoneyCommand;
import de.athoramine.core.commands.LevelUpCommand;
import de.athoramine.core.commands.LobbyCommand;
import de.athoramine.core.commands.LobbyItemCommand;
import de.athoramine.core.commands.MineCommand;
import de.athoramine.core.commands.MineFastTravelCommand;
import de.athoramine.core.commands.MineSetupCommand;
import de.athoramine.core.commands.PlotsCommand;
import de.athoramine.core.commands.SecretSetupCommand;
import de.athoramine.core.commands.SecretsCommand;
import de.athoramine.core.custom.items.tools.LobbyItem;
import de.athoramine.core.custom.items.tools.AdminPickaxe;
import de.athoramine.core.custom.items.tools.AdminSword;
import de.athoramine.core.database.DefaultDatabase;
import de.athoramine.core.database.DevDatabase;
import de.athoramine.core.database.GlobalDatabase;
import de.athoramine.core.database.ProductionDatabase;
import de.athoramine.core.listener.InventoryChange;
import de.athoramine.core.listener.PlayerChat;
import de.athoramine.core.listener.PlayerDeath;
import de.athoramine.core.listener.PlayerDestroyBlocks;
import de.athoramine.core.listener.PlayerFood;
import de.athoramine.core.listener.PlayerJoin;
import de.athoramine.core.listener.PlayerQuit;
import de.athoramine.core.listener.SeedsGrow;
import de.athoramine.core.util.configs.BankConfig;
import de.athoramine.core.util.manager.BossBarManager;
import de.athoramine.core.util.GameLoop;
import de.athoramine.core.util.ItemAPI;
import de.athoramine.core.util.SlowGameLoop;
import de.athoramine.core.util.configs.GeneralConfig;
import de.athoramine.core.util.manager.ExperienceManager;
import de.athoramine.core.util.manager.FarmingManager;
import de.athoramine.core.util.manager.ForagingManager;
import de.athoramine.core.util.manager.GeneralDestroyableBlocksManager;
import de.athoramine.core.util.manager.LeaderboardManager;
import de.athoramine.core.util.manager.LevelManager;
import de.athoramine.core.util.manager.MineManager;
import de.athoramine.core.util.manager.PlaytimeManager;
import de.athoramine.core.util.manager.SalaryManager;
import de.athoramine.core.util.manager.ScoreboardManager;
import de.athoramine.core.util.manager.SecretsManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import de.theamychan.scoreboard.network.Scoreboard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends PluginBase {

    private static Main main;

    private final ItemAPI itemAPI = new ItemAPI();

    public static final Map<Player, Scoreboard> scoreboards = new HashMap<>();

    @Override
    public void onLoad() {
        //load items
        PluginLogger log = new PluginLogger(this);
        try {
            Item.registerCustomItem(AdminSword.class);
            Item.registerCustomItem(AdminPickaxe.class);
            Item.registerCustomItem(LobbyItem.class);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.info("Register custom items failed!");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        Config generalConfig = new Config(new File(this.getDataFolder(), "generalConfig.yml"), Config.YAML);
        GeneralConfig.setGeneralConfig(generalConfig);

        initDatabases();
        initConfigs();

        //TODO check if required plugins enabled

//        if (getServer().getPluginManager().getPlugin("FuturePlots") != null) {
//            ScoreboardManager.plotsEnabled = true;
//            getLogger().info("Scoreboard Plots Mode aktiviert!");
//        }

        initCommands();
        initEvents();

        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new GameLoop(), 0, 20, true); // every second
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SlowGameLoop(), 0, 600, true); // every 30 seconds
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, () -> {
            Map<UUID, Player> players = Server.getInstance().getOnlinePlayers();
            if (!players.isEmpty()) {
                for (Player player : players.values()) {
                    ExperienceManager.saveExperience(player);
                }
            }
        }, 0, 12000, true);  // every 10 minutes


//        if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.LOBBY_SERVER)) {
//            getServer().getScheduler().scheduleDelayedTask(new ReloadLoop(), 216000);
//        } else if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
//            getServer().getScheduler().scheduleDelayedTask(new ReloadLoop(), 218400);
//        } else if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.DEV_SERVER)) {
//            getServer().getScheduler().scheduleDelayedTask(new ReloadLoop(), 220000);
//        } else {
//            getLogger().error("Cannot find Reload Time for active Server!");
//        }


        SecretsManager.loadSecrets();
        getLogger().info("Secrets wurden erfolgreich geladen!");
        GeneralDestroyableBlocksManager.renderDefaultView(getServer().getDefaultLevel());
        getLogger().info("GeneralDestroyableBlocks wurde erfolgreich zurückgesetzt!");
        FarmingManager.resetFarms(getServer().getDefaultLevel());
        getLogger().info("Farmen wurde erfolgreich zurückgesetzt!");
        MineManager.resetMine(getServer().getDefaultLevel());
        getLogger().info("Mine wurde erfolgreich zurückgesetzt!");
        MineManager.generateOres(getServer().getDefaultLevel());
        getLogger().info("Mine wurde erfolgreich erstellt!");

        LeaderboardManager.loadLeaderboards(getServer().getDefaultLevel());
    }

    private void initConfigs() {
        Config levelConfig = new Config(new File(this.getDataFolder(), "levelConfig.yml"), Config.YAML);
        Config mineConfig = new Config(new File(this.getDataFolder(), "mineConfig.yml"), Config.YAML);
        Config foragingConfig = new Config(new File(this.getDataFolder(), "foragingConfig.yml"), Config.YAML);
        Config farmingConfig = new Config(new File(this.getDataFolder(), "farmingConfig.yml"), Config.YAML);
        Config generalBlocksConfig = new Config(new File(this.getDataFolder(), "generalBlocksConfig.yml"), Config.YAML);
        Config bankConfig = new Config(new File(this.getDataFolder(), "bankConfig.yml"), Config.YAML);
        LevelManager.setLevelConfig(levelConfig);
        MineManager.setMineConfig(mineConfig);
        ForagingManager.setForagingConfig(foragingConfig);
        FarmingManager.config = farmingConfig;
        GeneralDestroyableBlocksManager.setConfig(generalBlocksConfig);
        BankConfig.setConfig(bankConfig);
    }

    private void initCommands() {
        getServer().getCommandMap().register("athoracore", new AthoraCoreCommand(this));
        getServer().getCommandMap().register("build", new BuildCommand(this));
        getServer().getCommandMap().register("levelup", new LevelUpCommand(this));
        getServer().getCommandMap().register("mine", new MineCommand(this));
        getServer().getCommandMap().register("minefasttravel", new MineFastTravelCommand(this));
        getServer().getCommandMap().register("minesetup", new MineSetupCommand(this));
        getServer().getCommandMap().register("farmsetup", new FarmSetupCommand(this));
        getServer().getCommandMap().register("generalblockssetup", new GeneralDestroyableBlocksSetupCommand(this));
        getServer().getCommandMap().register("secretsetup", new SecretSetupCommand(this));
        getServer().getCommandMap().register("secrets", new SecretsCommand(this));
        getServer().getCommandMap().register("lobby", new LobbyCommand(this));
        getServer().getCommandMap().register("plots", new PlotsCommand(this));
        getServer().getCommandMap().register("givemoney", new GiveMoneyCommand(this));
        getServer().getCommandMap().register("daily", new DailyCommand(this));
        getServer().getCommandMap().register("lobbyitem", new LobbyItemCommand(this));
        getServer().getCommandMap().register("gehalt", new GehaltCommand(this));
        getServer().getCommandMap().register("bank", new BankCommand(this));
    }

    private void initEvents() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);
        pluginManager.registerEvents(new PlayerDestroyBlocks(this), this);
        pluginManager.registerEvents(new PlayerChat(this), this);
//        pluginManager.registerEvents(new SeedsGrow(this), this);
        pluginManager.registerEvents(new PlayerDeath(this), this);
        pluginManager.registerEvents(new PlayerFood(this), this);
        pluginManager.registerEvents(new InventoryChange(this), this);
    }

    @Override
    public void onDisable() {
        try {
            Map<UUID, Player> players = Server.getInstance().getOnlinePlayers();
            if (!players.isEmpty()) {
                for (Player player : players.values()) {
                    getLogger().info("Playtime untrack " + player.getName() + " value: " + PlaytimeManager.playerTimes.get(player).toString());
                    PlaytimeManager.untrackPlayer(player);
                    getLogger().info("Playtime untrack " + player.getName() + " done!");
                    SalaryManager.untrackPlayer(player);
                    if (BossBarManager.playerHasBossBar(player)) {
                        getLogger().info("Remove BossBar for " + player.getName());
                        BossBarManager.removeBossBar(player);
                        getLogger().info("Remove BossBar DONE for " + player.getName());
                    }
                    //                    InventoryManager.savePlayerInventory(player, this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getLogger().info("Player Stuff done!");
        disconnectDatabases();
        getLogger().info("Disconnect Database done!");
        GeneralDestroyableBlocksManager.renderDefaultView(getServer().getDefaultLevel());
        getLogger().info("GeneralDestroyableBlocks wurde erfolgreich zurückgesetzt!");
        FarmingManager.resetFarms(getServer().getDefaultLevel());
        getLogger().info("Farmen wurden erfolgreich zurückgesetzt!");
        ForagingManager.resetForest(getServer().getDefaultLevel());
        getLogger().info("Wald wurde erfolgreich zurückgesetzt!");
        MineManager.resetMine(getServer().getDefaultLevel());
        getLogger().info("Mine wurde erfolgreich zurückgesetzt!");
    }

    public static Main getInstance() {
        return main;
    }

    public ItemAPI getItemAPI() {
        return itemAPI;
    }

    private void disconnectDatabases() {
        DefaultDatabase.disconnect();
        GlobalDatabase.disconnect();
        DevDatabase.disconnect();
        ProductionDatabase.disconnect();
    }

    private void initDatabases() {
        DefaultDatabase.connect();
        if (!DefaultDatabase.isConnected())
            getLogger().error("Failed to connect to Default Database!");
        GlobalDatabase.connect();
        if (!GlobalDatabase.isConnected())
            getLogger().error("Failed to connect to Global Database!");
        DevDatabase.connect();
        if (!DevDatabase.isConnected())
            getLogger().error("Failed to connect to Dev Database!");
        ProductionDatabase.connect();
        if (!ProductionDatabase.isConnected())
            getLogger().error("Failed to connect to Production Database!");
    }
}
