package de.athoramine.core.commands;

import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.SecretsManager;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class SecretSetupCommand extends PluginCommand<Main> {

    public SecretSetupCommand(Main owner) {
        super("secretsetup", owner);
        this.setDescription("Füge neue Secrets hinzu oder lösche alte!");
        this.setPermission("athora.secrets.setup.command");
        this.setPermissionMessage(Vars.PREFIX + "Du hast keine Berechtigung diesen Befehl auszuführen!");
        this.commandParameters.clear();
        this.commandParameters.put("secret->setup->add", new CommandParameter[]{
                CommandParameter.newEnum("SetupOption", new CommandEnum("SetupAddSecret", "add")),
                CommandParameter.newType("CoinsReward", CommandParamType.INT),
                CommandParameter.newType("RuhmReward", CommandParamType.INT)
        });
        this.commandParameters.put("secret->setup->remove", new CommandParameter[]{
                CommandParameter.newEnum("SetupOption", new CommandEnum("SetupRemoveSecret", "remove"))
        });
        this.commandParameters.put("secret->setup->exit", new CommandParameter[]{
                CommandParameter.newEnum("SetupOption", new CommandEnum("SetupRemoveSecret", "exit"))
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.secrets.setup.command")) {
            sender.sendMessage(Vars.PREFIX + "Du hast keine Berechtigung diesen Befehl auszuführen!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /secretsetup [add|remove]");
            return false;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /secretsetup add [coinsReward [ruhmReward]");
                return false;
            }
            if (SecretsManager.setupAddList.containsKey(player) || SecretsManager.setupRemoveList.containsKey(player)) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du bist bereits im Secret Setup Mode!");
                return false;
            }
            int[] rewards = new int[]{Integer.parseInt(args[1]), Integer.parseInt(args[2])};
            SecretsManager.setupAddList.put(player, rewards);
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Klicke jetzt auf ein Schild um es als Secret hinzuzufügen!");
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (SecretsManager.setupAddList.containsKey(player) || SecretsManager.setupRemoveList.containsKey(player)) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du bist bereits im Secret Setup Mode!");
                return false;
            }
            SecretsManager.setupRemoveList.put(player, true);
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Klicke jetzt auf ein Secret um es zu entfernen!");
            return true;
        }
        if (args[0].equalsIgnoreCase("exit")) {
            SecretsManager.setupAddList.remove(player);
            SecretsManager.setupRemoveList.remove(player);
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du bist jetzt nicht mehr im Setup Mode!");
            return true;
        }
        player.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /secretsetup [add|remove]");
        return false;
    }
}
