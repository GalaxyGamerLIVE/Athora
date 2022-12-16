package de.athoramine.core.commands;

import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.LobbyItemManager;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

public class LobbyItemCommand extends PluginCommand<Main> {

    public LobbyItemCommand(Main owner) {
        super("lobbyitem", owner);
        this.setDescription("Schaltet das Lobby Item an oder aus!");
        this.commandParameters.clear();
        this.commandParameters.put("lobbyitem->toggle", new CommandParameter[]{
                CommandParameter.newEnum("LobbyItemStatus", new CommandEnum("Status", "on", "off"))
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isPlayer()) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du bist kein Spieler!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /lobbyitem on/off");
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            if (LobbyItemManager.hasLobbyItem(player)) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast bereits ein Lobby Item!");
                return false;
            }
            LobbyItemManager.setLobbyItem(player);
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Dir wurde jetzt ein Lobby Item gegeben!");
        } else if (args[0].equalsIgnoreCase("off")) {
            if (!LobbyItemManager.hasLobbyItem(player)) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast kein Lobby Item!");
                return false;
            }
            player.getInventory().setItem(LobbyItemManager.getLobbyItemSlot(player), new Item(0));
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Das Lobby Item wurde erfolgreich entfernt!");
        } else {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /lobbyitem on/off");
            return false;
        }
        return true;
    }
}
