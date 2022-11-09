package de.athoramine.core.commands;

import de.athoramine.core.forms.DailyQuest.DailyQuestForm;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.DailyQuestManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class DailyCommand extends PluginCommand<Main> {
    public DailyCommand(Main owner) {
        super("daily", owner);
        setPermission("athora.command.daily");
        setPermissionMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszufzuführen!");
        setDescription("Öffnet das Daily Quest Menü!");
        this.commandParameters.clear();
        this.commandParameters.put("target", new CommandParameter[]{CommandParameter.newType("targetPlayer", CommandParamType.TARGET)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player targetPlayer;
        if (!sender.hasPermission("athora.command.daily")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszufzuführen!");
            return false;
        }
        if (args.length > 0) {
            if (Server.getInstance().getPlayer(args[0]) != null) {
                targetPlayer = Server.getInstance().getPlayer(args[0]).getPlayer();
            } else {
                sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Der Spieler " + args[0] + " wurde nicht gefunden!");
                return false;
            }
        } else if (sender.isPlayer()) {
            targetPlayer = (Player) sender;
        } else {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du bist kein Spieler!");
            return false;
        }

        if (DailyQuestManager.hasActiveQuest(targetPlayer)) {
            DailyQuestManager.collectQuestItemsForActiveQuest(targetPlayer);
        } else {
            new DailyQuestForm(targetPlayer);
        }

        return true;
    }
}
