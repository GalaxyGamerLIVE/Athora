package de.athoramine.core.util.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;

public class WorldManager {

    public static final String LOBBY = "lobby";
    public static final String PLOT = "plots1";
    public static final String DEV = "lobby";

    public static void sendPlayerToLevel(Player player, String levelName) {
        Level destinationLevel = Server.getInstance().getLevelByName(levelName);
        player.teleport(destinationLevel.getSpawnLocation());
    }

    // TODO check if some isInWorld request not needed anymore because switching server setup to a world setup
    public static boolean isInWorld(Player player, String world) {
        return player.getLevelName().equalsIgnoreCase(world);
    }


}
