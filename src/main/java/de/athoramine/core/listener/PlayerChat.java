package de.athoramine.core.listener;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.manager.LevelManager;
import de.athoramine.core.util.manager.PermissionManager;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;

public class PlayerChat implements Listener {

    private final Main plugin;

    public PlayerChat(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String group = PermissionManager.getGroup(player);
        String format = "";
        if (group.equalsIgnoreCase("Default")) {
            format = LevelManager.getRoleName(AthoraPlayer.getLevel(player)) + ": §r§7" + player.getName() + " §o[§r" + AthoraPlayer.getLevel(player) + "§r§7§o]§r§7 >> " + message;
        }else {
            format = "§7[§r" + PermissionManager.getColorCodeFromGroup(player) + PermissionManager.getGroup(player) + "§7] " + LevelManager.getRoleName(AthoraPlayer.getLevel(player)) + ": §r§7" + player.getName() + " §o[§r" + AthoraPlayer.getLevel(player) + "§r§7§o]§r§7 >> " + message;
        }
        event.setFormat(TextFormat.colorize('&', format));
    }

}
