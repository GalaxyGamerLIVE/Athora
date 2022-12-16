package de.athoramine.core.commands;

import de.athoramine.core.forms.Bank.BankMainForm;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class BankCommand extends PluginCommand<Main> {

    public BankCommand(Main owner) {
        super("bank", owner);
        this.setDescription("Öffnet das Bank Menü für einen Spieler!");
        this.setPermission("athora.bank.gui.command");
        this.commandParameters.clear();
        this.commandParameters.put("target", new CommandParameter[]{
                CommandParameter.newType("targetPlayer", CommandParamType.TARGET)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.bank.gui.command")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst einen Spieler angeben!");
            return false;
        }

        Player targetPlayer;
        if (this.getPlugin().getServer().getPlayer(args[0]) != null) {
            targetPlayer = this.getPlugin().getServer().getPlayer(args[0]);
        } else {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Der Spieler wurde nicht gefunden!");
            return false;
        }

        Helper.playSound("mob.villager.haggle", targetPlayer, 0.3f, 0.8f);
        new BankMainForm(targetPlayer);

        return true;
    }
}
