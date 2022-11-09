package de.athoramine.core.util;

import de.athoramine.core.util.manager.InterestManager;
import de.athoramine.core.util.manager.LeaderboardManager;
import cn.nukkit.Player;
import cn.nukkit.Server;

import java.util.Map;
import java.util.UUID;

public class SlowGameLoop implements Runnable {
    @Override
    public void run() {
        InterestManager.checkInterestDistribute();
        try {
            Map<UUID, Player> players = Server.getInstance().getOnlinePlayers();
            if (!players.isEmpty()) {
                for (Player player : players.values()) {
                    InterestManager.checkInterest(player, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LeaderboardManager.loadLeaderboards(Server.getInstance().getDefaultLevel());
    }
}
