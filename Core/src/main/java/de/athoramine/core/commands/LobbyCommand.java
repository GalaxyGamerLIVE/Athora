package de.athoramine.core.commands;

import de.athoramine.core.util.manager.WorldManager;
import de.athoramine.core.main.Main;
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
        WorldManager.sendPlayerToLevel(player, WorldManager.LOBBY);
        return true;
    }
}
