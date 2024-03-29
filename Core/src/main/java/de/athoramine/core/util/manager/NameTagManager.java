package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import cn.nukkit.Player;

public class NameTagManager {

    public static void updateNameTag(Player player) {
        if (player.isOnline()) {
            String format = "§e" + player.getName() + " §o§7[§r" + AthoraPlayer.getLevel(player) + "§r§7§o]§r\n§6Ruhm: §g" + Helper.round(AthoraPlayer.getRuhm(player), 2) + " \uE101";
            player.setNameTag(format);
        }
    }

}
