package AthoraCore.main;

import AthoraCore.commands.AthoraCoreCommand;
import AthoraCore.commands.BuildCommand;
import AthoraCore.commands.DailyCommand;
import AthoraCore.commands.FarmSetupCommand;
import AthoraCore.commands.GeneralDestroyableBlocksSetupCommand;
import AthoraCore.commands.GiveMoneyCommand;
import AthoraCore.commands.LevelUpCommand;
import AthoraCore.commands.LobbyCommand;
import AthoraCore.commands.MineCommand;
import AthoraCore.commands.MineFastTravelCommand;
import AthoraCore.commands.MineSetupCommand;
import AthoraCore.commands.PlotsCommand;
import AthoraCore.commands.SecretSetupCommand;
import AthoraCore.commands.SecretsCommand;
import AthoraCore.database.DefaultDatabase;
import AthoraCore.database.DevDatabase;
import AthoraCore.database.GlobalDatabase;
import AthoraCore.database.ProductionDatabase;
import AthoraCore.listener.PlayerChat;
import AthoraCore.listener.PlayerDeath;
import AthoraCore.listener.PlayerDestroyBlocks;
import AthoraCore.listener.PlayerFood;
import AthoraCore.listener.PlayerJoin;
import AthoraCore.listener.ListenDataPacket;
import AthoraCore.listener.PlayerQuit;
import AthoraCore.listener.SeedsGrow;
import AthoraCore.util.manager.BossBarManager;
import AthoraCore.util.GameLoop;
import AthoraCore.util.ItemAPI;
import AthoraCore.util.SlowGameLoop;
import AthoraCore.util.configs.GeneralConfig;
import AthoraCore.util.manager.ExperienceManager;
import AthoraCore.util.manager.FarmingManager;
import AthoraCore.util.manager.ForagingManager;
import AthoraCore.util.manager.GeneralDestroyableBlocksManager;
import AthoraCore.util.manager.LeaderboardManager;
import AthoraCore.util.manager.LevelManager;
import AthoraCore.util.manager.MineManager;
import AthoraCore.util.manager.PlaytimeManager;
import AthoraCore.util.manager.ReloadLoop;
import AthoraCore.util.manager.ScoreboardManager;
import AthoraCore.util.manager.SecretsManager;
import AthoraCore.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import de.theamychan.scoreboard.network.Scoreboard;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends PluginBase {

    private static Main main;

    private final ItemAPI itemAPI = new ItemAPI();

    public static final Map<Player, Scoreboard> scoreboards = new HashMap<>();

    @Override
    public void onEnable() {
        Config generalConfig = new Config(new File(this.getDataFolder(), "generalConfig.yml"), Config.YAML);
        GeneralConfig.setGeneralConfig(generalConfig);
        initDatabases();
        Config levelConfig = new Config(new File(this.getDataFolder(), "levelConfig.yml"), Config.YAML);
        Config mineConfig = new Config(new File(this.getDataFolder(), "mineConfig.yml"), Config.YAML);
        Config foragingConfig = new Config(new File(this.getDataFolder(), "foragingConfig.yml"), Config.YAML);
        Config farmingConfig = new Config(new File(this.getDataFolder(), "farmingConfig.yml"), Config.YAML);
        Config generalBlocksConfig = new Config(new File(this.getDataFolder(), "generalBlocksConfig.yml"), Config.YAML);
        LevelManager.setLevelConfig(levelConfig);
        MineManager.setMineConfig(mineConfig);
        ForagingManager.setForagingConfig(foragingConfig);
        FarmingManager.config = farmingConfig;
        GeneralDestroyableBlocksManager.setConfig(generalBlocksConfig);

//        saveDefaultConfig();
//        LevelManager.setLevelConfig(getConfig());

        if (getServer().getPluginManager().getPlugin("FuturePlots") != null) {
            ScoreboardManager.plotsEnabled = true;
            getLogger().info("Scoreboard Plots Mode aktiviert!");
        }

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

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(this), this);
//        pluginManager.registerEvents(new PlayerMove(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);
        pluginManager.registerEvents(new PlayerDestroyBlocks(this), this);
        pluginManager.registerEvents(new PlayerChat(this), this);
        pluginManager.registerEvents(new SeedsGrow(this), this);
        pluginManager.registerEvents(new PlayerDeath(this), this);
        pluginManager.registerEvents(new PlayerFood(this), this);
//        pluginManager.registerEvents(new ListenDataPacket(this), this);

        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new GameLoop(), 0, 50, true);
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SlowGameLoop(), 0, 3000, true);
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, () -> {
            Map<UUID, Player> players = Server.getInstance().getOnlinePlayers();
            if (!players.isEmpty()) {
                for (Player player : players.values()) {
                    ExperienceManager.saveExperience(player);
                }
            }
        }, 0, 12000, true);


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

    @Override
    public void onDisable() {
        try {
            Map<UUID, Player> players = Server.getInstance().getOnlinePlayers();
            if (!players.isEmpty()) {
                for (Player player : players.values()) {
                    PlaytimeManager.untrackPlayer(player);
                    if (BossBarManager.playerHasBossBar(player))
                        BossBarManager.removeBossBar(player);
                    //                    InventoryManager.savePlayerInventory(player, this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectDatabases();
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
