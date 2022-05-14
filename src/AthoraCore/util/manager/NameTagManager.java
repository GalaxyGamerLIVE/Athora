package AthoraCore.util.manager;

import AthoraCore.api.AthoraPlayer;
import cn.nukkit.Player;

public class NameTagManager {

    public static void updateNameTag(Player player) {
        if (player.isOnline()) {
            String format = "§e" + player.getName() + " §o§7[§r" + AthoraPlayer.getLevel(player) + "§r§7§o]§r\n§6Ruhm: §g" + AthoraPlayer.getRuhm(player) + " \uE101";
            player.setNameTag(format);
        }
    }

}
