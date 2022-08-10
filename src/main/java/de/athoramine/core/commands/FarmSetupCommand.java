package de.athoramine.core.commands;

import de.athoramine.core.main.Main;
import de.athoramine.core.util.manager.FarmingManager;
import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class FarmSetupCommand extends PluginCommand<Main> {

    public FarmSetupCommand(Main owner) {
        super("farmsetup", owner);
        this.setDescription("Füge neue Felder hinzu oder entferne alte!");
        this.setPermission("athora.farm.setup.command");
        this.setPermissionMessage(Vars.PREFIX + "Du hast keine Berechtigung diesen Befehl auszuführen!");
        this.commandParameters.clear();
        this.commandParameters.put("farm->setup->exit", new CommandParameter[]{
                CommandParameter.newEnum("SetupExit", new CommandEnum("FarmingManagerPlant", "exit"))
        });
        for (String plant : FarmingManager.plants) {
            this.commandParameters.put("farm->setup->" + plant, new CommandParameter[]{
                    CommandParameter.newEnum("FarmingPlant", new CommandEnum("FarmingManagerPlant", plant))
            });
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.farm.setup.command")) {
            sender.sendMessage(Vars.PREFIX + "Du hast keine Berechtigung diesen Befehl auszuführen!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /farmsetup [option]");
            return false;
        }
        if (args[0].equalsIgnoreCase("exit")) {
            FarmingManager.removePlayerFromSetupList(player);
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast den Farming Setup Mode verlassen!");
            return true;
        }
        if (FarmingManager.plantExist(args[0])) {
            if (FarmingManager.setPlayerToSetupList(player, args[0])) {
                player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast den Farming Setup Mode betreten! Pflanze: " + TextFormat.BLUE + args[0]);
                return true;
            } else {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /farmsetup [option]");
                return false;
            }
        }
        player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /farmsetup [option]");
        return false;
    }

}
