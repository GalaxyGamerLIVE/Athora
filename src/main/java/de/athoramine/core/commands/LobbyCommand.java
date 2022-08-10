package de.athoramine.core.commands;

import de.athoramine.core.main.Main;
import de.athoramine.core.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;

public class LobbyCommand extends PluginCommand<Main> {

    public LobbyCommand(Main owner) {
        super("lobby", owner);
        this.setDescription("Teleportiert dich zum Lobby Server!");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isPlayer()) {
            return false;
        }
        Player player = (Player) sender;
        ServerManager.sendPlayerToServer(player, ServerManager.LOBBY_SERVER);
        return true;
    }
}
