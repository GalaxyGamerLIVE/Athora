package de.athoramine.core.commands;

import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.GeneralDestroyableBlocksManager;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class GeneralDestroyableBlocksSetupCommand extends PluginCommand<Main> {

    public GeneralDestroyableBlocksSetupCommand(Main owner) {
        super("generalblockssetup", owner);
        this.setDescription("Füge neue Farm Blöcke in der Welt hinzu oder entferne sie!");
        this.setPermission("athora.generalblocks.setup.command");
        this.setPermissionMessage(Vars.PREFIX + "Du hast keine Berechtigung diesen Befehl auszuführen!");
        this.commandParameters.clear();
        this.commandParameters.put("generalBlocks->setup->exit", new CommandParameter[]{
                CommandParameter.newEnum("SetupExit", new CommandEnum("GeneralDestroyableBlocksManagerBlock", "exit")),
        });
        for (int blockID : GeneralDestroyableBlocksManager.destroyableBlocks) {
            String blockName = GeneralDestroyableBlocksManager.getBlockName(blockID);
            this.commandParameters.put("farm->setup->" + blockName, new CommandParameter[]{
                    CommandParameter.newEnum("GeneralBlock", new CommandEnum("GeneralDestroyableBlocksManagerBlock", blockName)),
            });
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.generalblocks.setup.command")) {
            sender.sendMessage(Vars.PREFIX + "Du hast keine Berechtigung diesen Befehl auszuführen!");
            return false;
        }
        Player player = (Player) sender;
        StringBuilder helpMessage = new StringBuilder(Vars.PREFIX + TextFormat.RED + "Benutze: /generalblockssetup [");
        for (int i = 0; i < GeneralDestroyableBlocksManager.destroyableBlocks.length; i++) {
            int blockID = GeneralDestroyableBlocksManager.destroyableBlocks[i];
            String blockName = GeneralDestroyableBlocksManager.getBlockName(blockID);
            if (i == GeneralDestroyableBlocksManager.destroyableBlocks.length - 1) {
                helpMessage.append(blockName + "]");
            } else {
                helpMessage.append(blockName + "|");
            }
        }
        if (args.length == 0) {
            player.sendMessage(helpMessage.toString());
            return false;
        }
        if (args[0].equalsIgnoreCase("exit")) {
            GeneralDestroyableBlocksManager.removePlayerFromSetupList(player);
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast den General Blocks Setup Mode verlassen!");
            return true;
        }
        if (GeneralDestroyableBlocksManager.isBlockNameInDestroyableBlocks(args[0])) {
            if (GeneralDestroyableBlocksManager.setPlayerToSetupList(player, args[0])) {
                player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast den General Blocks Setup Mode betreten! Block: " + TextFormat.BLUE + args[0]);
                return true;
            } else {
                player.sendMessage(helpMessage.toString());
                return false;
            }
        }
        player.sendMessage(helpMessage.toString());
        return false;
    }

}
