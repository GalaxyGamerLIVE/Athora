package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlaytimeManager {

    public static final Map<Player, Long> playerTimes = new HashMap<>();

    public static void trackPlayer(Player player) {
        try {
            if (!PlaytimeManager.playerTimes.containsKey(player)) {
                PlaytimeManager.playerTimes.put(player, System.currentTimeMillis());
            } else {
                Server.getInstance().getLogger().warning("PlaytimeManager try to add a Player to the Map but the Player exists already in the Map.");
            }
        } catch (NullPointerException ignored) {

        }
    }

    public static void untrackPlayer(Player player) {
        if (PlaytimeManager.playerTimes.containsKey(player)) {
            AthoraPlayer.setPlaytime(player, PlaytimeManager.getTotalPlaytime(player));
            PlaytimeManager.playerTimes.remove(player);
        } else {
            Server.getInstance().getLogger().warning("PlaytimeManager try to remove a Player from the Map but the Player not exists in the Map.");
        }
    }

    public static long getTotalPlaytime(Player player) {
        if (!PlaytimeManager.playerTimes.containsKey(player)) {
            PlaytimeManager.playerTimes.put(player, System.currentTimeMillis());
        }
        long newPlaytime = System.currentTimeMillis() - PlaytimeManager.playerTimes.get(player);
        return AthoraPlayer.getPlaytime(player) + newPlaytime;
    }

    public static String getTotalPlaytimeFormatted(Player player) {
        return String.format("%dh", TimeUnit.MILLISECONDS.toHours(getTotalPlaytime(player)));
    }

}
