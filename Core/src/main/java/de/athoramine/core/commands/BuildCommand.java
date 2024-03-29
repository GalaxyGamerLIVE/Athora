package de.athoramine.core.commands;

import cn.nukkit.command.PluginCommand;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.BuildManager;
import de.athoramine.core.util.manager.WorldManager;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class BuildCommand extends PluginCommand<Main> {

    public BuildCommand(Main owner) {
        super("build", owner);
        this.setPermission("athora.build.command");
        this.setPermissionMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
        this.setDescription("Aktiviere oder deaktiviere den Baumodus!");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("athora.build.command")) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
            return false;
        }
        if (WorldManager.isInWorld(player, WorldManager.PLOT)) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Der Baumodus ist auf dem Plots Server deaktiviert!");
            return false;
        }
        boolean buildModeEnabled = BuildManager.toggleBuildMode(player);
        if (buildModeEnabled) {
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Baumodus wurde erfolgreich aktiviert!");
        } else {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Der Baumodus wurde erfolgreich deaktiviert!");
        }
        return true;
    }

}
