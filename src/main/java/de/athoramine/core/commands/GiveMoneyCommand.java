package de.athoramine.core.commands;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class GiveMoneyCommand extends PluginCommand<Main> {

    public GiveMoneyCommand(Main owner) {
        super("givemoney", owner);
        this.setDescription("Mit den Befehl kanns du anderen Spielern dein Geld geben!");
        String[] aliases = new String[]{"pay"};
        this.setAliases(aliases);
        this.commandParameters.clear();
        this.commandParameters.put("money->give", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("money", CommandParamType.FLOAT)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /givemoney <Player> <Money>");
            return false;
        }

        if (Server.getInstance().getPlayer(args[0]) == null) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Der Spieler " + TextFormat.AQUA + args[0] + TextFormat.RED + " ist nicht online!");
            return false;
        }

        Player targetPlayer = Server.getInstance().getPlayer(args[0]).getPlayer();

        if (player.getName().equalsIgnoreCase(targetPlayer.getName())) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du kannst dir nicht selber Geld geben!");
            return false;
        }

        if (!Helper.isNumeric(args[1])) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Gebe eine Zahl als Geldwert an!");
            return false;
        }

        double money = Double.parseDouble(args[1]);

        if (AthoraPlayer.getPurse(player) < money) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast nicht genug Geld dabei!");
            return false;
        }

        if (money >= 1) {
            AthoraPlayer.setPurse(player, AthoraPlayer.getPurse(player) - money);
            AthoraPlayer.setPurse(targetPlayer, AthoraPlayer.getPurse(targetPlayer) + money);
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich den Spieler " + TextFormat.AQUA + targetPlayer.getName() + " " + TextFormat.GOLD + money + "$ " + TextFormat.GREEN + "gegeben!");
            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.AQUA + player.getName() + TextFormat.GREEN + " hat dir " + TextFormat.GOLD + money + "$ " + TextFormat.GREEN + "gegeben!");
        } else {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst eine Zahl eingeben die höher als 0 ist!");
            return false;
        }

        return true;
    }
}
