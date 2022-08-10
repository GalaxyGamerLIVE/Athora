package de.athoramine.core.util.manager;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;
import java.util.HashMap;
import java.util.UUID;

public class BossBarManager {
    private static HashMap<UUID, Long> bossBars = new HashMap<>();

    public static void addBossBar(Player player, DummyBossBar bossBar) {
        bossBars.put(player.getUniqueId(), Long.valueOf(bossBar.getBossBarId()));
    }

    public static void removeBossBar(Player player) {
        if (playerHasBossBar(player)) {
            DummyBossBar bossBar = getPlayerBossBar(player);
            bossBar.destroy();
            bossBars.remove(player.getUniqueId());
        }
    }

    public static boolean playerHasBossBar(Player player) {
        Long id = bossBars.get(player.getUniqueId());
        return (id != null);
    }

    public static DummyBossBar getPlayerBossBar(Player player) {
        Long id = bossBars.get(player.getUniqueId());
        return player.getDummyBossBar(id.longValue());
    }
}
