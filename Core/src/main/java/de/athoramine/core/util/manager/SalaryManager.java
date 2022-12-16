package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;

import java.util.HashMap;
import java.util.Map;

public class SalaryManager {

    private static final Map<Player, Long> sessionTimes = new HashMap<>();
    private static final Map<Player, Boolean> salaryFinished = new HashMap<>();
    public static final long MIN_SALARY_TIME = (30 * 60000);

    public static void trackPlayer(Player player) {
        try {
            if (!SalaryManager.sessionTimes.containsKey(player)) {
                SalaryManager.sessionTimes.put(player, System.currentTimeMillis());
            } else {
                Server.getInstance().getLogger().warning("SalaryManager try to add a Player to the Map but the Player exists already in the Map.");
            }
        } catch (NullPointerException ignored) {

        }
    }

    public static void untrackPlayer(Player player) {
        try {
            if (SalaryManager.sessionTimes.containsKey(player)) {
                AthoraPlayer.setSalaryTime(player, (AthoraPlayer.getSalaryTime(player) + SalaryManager.getSessionTime(player)));
                SalaryManager.sessionTimes.remove(player);
            } else {
                Server.getInstance().getLogger().warning("SalaryManager try to remove a Player from the Map but the Player not exists in the Map.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPlayerTracked(Player player) {
        return SalaryManager.sessionTimes.containsKey(player);
    }

    public static long getTotalPlaytime(Player player) {
        return AthoraPlayer.getSalaryTime(player) + SalaryManager.getSessionTime(player);
    }

    public static long getSessionTime(Player player) {
        if (!SalaryManager.sessionTimes.containsKey(player)) {
            SalaryManager.sessionTimes.put(player, System.currentTimeMillis());
        }
        return System.currentTimeMillis() - SalaryManager.sessionTimes.get(player);
    }

    public static void resetSalaryStats(Player player) {
        if (SalaryManager.sessionTimes.containsKey(player)) {
            SalaryManager.sessionTimes.replace(player, System.currentTimeMillis());
            AthoraPlayer.setSalaryTime(player, 0);
        } else {
            Server.getInstance().getLogger().warning("SalaryManager try to remove a Player from the Map but the Player not exists in the Map.");
        }
    }

    public static boolean reachedMinSalaryTime(Player player) {
        // 30 Minutes in Milliseconds
        return SalaryManager.getTotalPlaytime(player) >= SalaryManager.MIN_SALARY_TIME;
    }

    public static boolean isSalaryFinished(Player player) {
        if (!SalaryManager.salaryFinished.containsKey(player)) {
            return false;
        }
        return SalaryManager.salaryFinished.get(player);
    }

    public static void setPlayerToSalaryFinished(Player player) {
        if (!SalaryManager.salaryFinished.containsKey(player)) {
            SalaryManager.salaryFinished.put(player, true);
        } else {
            SalaryManager.salaryFinished.replace(player, true);
        }
    }

    public static void setPlayerToNotSalaryFinished(Player player) {
        if (!SalaryManager.salaryFinished.containsKey(player)) {
            SalaryManager.salaryFinished.put(player, false);
        } else {
            SalaryManager.salaryFinished.replace(player, false);
        }
    }

}

