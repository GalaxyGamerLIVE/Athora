package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import cn.nukkit.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExperienceManager {

    public static Map<UUID, Boolean> loadedPlayers = new HashMap<>();

    public static void saveExperience(Player player) {
        AthoraPlayer.setExperienceLevel(player, player.getExperienceLevel());
        AthoraPlayer.setExperienceValue(player, player.getExperience());
    }

    public static void loadExperience(Player player) {
        player.setExperience(AthoraPlayer.getExperienceValue(player), AthoraPlayer.getExperienceLevel(player));
        ExperienceManager.loadedPlayers.put(player.getUniqueId(), true);
    }

    public static boolean isPlayerLoaded(Player player) {
        if (loadedPlayers.containsKey(player.getUniqueId())) return loadedPlayers.get(player.getUniqueId());
        return false;
    }

}
