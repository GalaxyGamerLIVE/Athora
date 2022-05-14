package AthoraCore.util.manager;

import cn.nukkit.Player;

import java.util.HashMap;
import java.util.Map;

public class BuildManager {

    private static final Map<Player, Boolean> buildMap = new HashMap<>();

    public static boolean toggleBuildMode(Player player) {
        if (buildMap.containsKey(player)) {
            buildMap.replace(player, (!buildMap.get(player)));
        } else {
            buildMap.put(player, true);
        }
        return buildMap.get(player);
    }

    public static boolean getState(Player player) {
        if (buildMap.containsKey(player)) {
            return buildMap.get(player);
        }
        return false;
    }

}
