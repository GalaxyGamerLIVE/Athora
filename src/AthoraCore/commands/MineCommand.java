package AthoraCore.commands;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.main.Main;
import AthoraCore.util.GameLoop;
import AthoraCore.util.manager.MineManager;
import AthoraCore.util.Vars;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;
import java.util.UUID;

public class MineCommand extends PluginCommand<Main> {

    public MineCommand(Main owner) {
        super("mine", owner);
        this.setPermission("athora.mine.command");
        this.setPermissionMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
        this.setDescription("Betritt eine Mine!");

        this.commandParameters.clear();
        this.commandParameters.put("section", new CommandParameter[]{
                CommandParameter.newType("section", CommandParamType.INT),
                CommandParameter.newEnum("direction", new CommandEnum("AthoraMineDirection", "start", "end")),
                CommandParameter.newType("targetPlayer", CommandParamType.TARGET)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.mine.command")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /mine [Bereich] [start/end] [Spieler]");
            return false;
        }
        String[] mines = {"1", "2", "3", "4", "5", "6"};
        if (!Arrays.asList(mines).contains(args[0])) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /mine [Bereich] [start/end] [Spieler]");
            return false;
        }
        if (!args[1].equalsIgnoreCase("start") && !args[1].equalsIgnoreCase("end")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze: /mine [Bereich] [start/end] [Spieler]");
            return false;
        }
        UUID targetUUID = null;
        if (this.getPlugin().getServer().getPlayer(args[2]) == null) {
            if (AthoraPlayer.getUUIDbyPlayerName(args[2]) == null) {
                sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Spieler wurde nicht gefunden!");
                return false;
            }
            targetUUID = UUID.fromString(AthoraPlayer.getUUIDbyPlayerName(args[2]));
        } else {
            targetUUID = this.getPlugin().getServer().getPlayer(args[2]).getUniqueId();
        }

        if (AthoraPlayer.getLevel(targetUUID) >= MineManager.getMineRequirements(Integer.parseInt(args[0]))) {
            this.getPlugin().getServer().getPlayer(args[2]).teleport(MineManager.getMineSpawnpoint(Integer.parseInt(args[0]), args[1]));
            this.getPlugin().getServer().getPlayer(args[2]).setYaw(MineManager.getMineSpawnYaw(Integer.parseInt(args[0]), args[1]));
            this.getPlugin().getServer().getPlayer(args[2]).setPitch(MineManager.getMineSpawnPitch(Integer.parseInt(args[0]), args[1]));
            this.getPlugin().getServer().getPlayer(args[2]).sendTitle(TextFormat.GREEN + "Mine " + args[0]);
        } else {
            this.getPlugin().getServer().getPlayer(args[2]).sendMessage(Vars.PREFIX + TextFormat.RED + "Du brauchst Level " + TextFormat.BLUE + MineManager.getMineRequirements(Integer.parseInt(args[0])) + TextFormat.RED + " um in die Mine zu kommen!");
        }

        return true;
    }
}
