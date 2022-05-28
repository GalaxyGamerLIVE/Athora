package AthoraCore.main;

import AthoraCore.commands.*;
import AthoraCore.listener.*;
import AthoraCore.util.*;
import AthoraCore.util.configs.GeneralConfig;
import AthoraCore.util.manager.*;
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

        Database.connect();
        getLogger().info("Datenbank Verbindung aufgebaut!");

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

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(this), this);
//        pluginManager.registerEvents(new PlayerMove(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);
        pluginManager.registerEvents(new PlayerDestroyBlocks(this), this);
        pluginManager.registerEvents(new PlayerChat(this), this);
        pluginManager.registerEvents(new SeedsGrow(this), this);
        pluginManager.registerEvents(new PlayerDeath(this), this);
        pluginManager.registerEvents(new PlayerFood(this), this);

        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new GameLoop(), 0, 50, true);
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SlowGameLoop(), 0, 3000, true);

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
//                    InventoryManager.savePlayerInventory(player, this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Database.disconnect();
        getLogger().info("Datenbank Verbindung geschlossen!");

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

}


