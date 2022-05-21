package AthoraCore.util;

import AthoraCore.util.manager.InterestManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

import java.util.Map;
import java.util.UUID;

public class SlowGameLoop implements Runnable {
    @Override
    public void run() {
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

    }
}
