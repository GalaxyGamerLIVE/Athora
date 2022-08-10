package de.athoramine.core.commands;

import de.athoramine.core.forms.MineFastTravelForm;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class MineFastTravelCommand extends PluginCommand<Main> {

    private final Main plugin;

    public MineFastTravelCommand(Main owner) {
        super("minefasttravel", owner);
        plugin = owner;
        this.setPermission("athora.minefasttravel.command");
        this.setPermissionMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
        this.setDescription("Öffnet das Schnellreise Menü für die Mine!");

        this.commandParameters.clear();
        this.commandParameters.put("target", new CommandParameter[]{
                CommandParameter.newType("targetPlayer", CommandParamType.TARGET)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.minefasttravel.command")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
            return false;
        }

        Player targetPlayer;
        if (args.length > 0 && this.getPlugin().getServer().getPlayer(args[0]) != null) {
            targetPlayer = this.getPlugin().getServer().getPlayer(args[0]);
        } else {
            targetPlayer = (Player) sender;
        }

        new MineFastTravelForm(targetPlayer);

        return true;
    }
}
