package AthoraCore.commands;

import AthoraCore.main.Main;
import AthoraCore.util.manager.BuildManager;
import AthoraCore.util.Vars;
import AthoraCore.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
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
        if(!player.hasPermission("athora.build.command")) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
            return false;
        }
        if(ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
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
