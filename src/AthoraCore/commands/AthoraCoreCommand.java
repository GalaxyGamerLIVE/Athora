package AthoraCore.commands;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.main.Main;
import AthoraCore.util.Helper;
import AthoraCore.util.Vars;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;

public class AthoraCoreCommand extends PluginCommand<Main> {
    public AthoraCoreCommand(Main owner) {
        super("athoracore", owner);
        this.setPermission("athora.core.api.commands");
        this.setPermissionMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
        this.setDescription("Verändere Werte im Core System direkt im Spiel!");

        this.commandParameters.clear();
        this.commandParameters.put("help", new CommandParameter[]{
                CommandParameter.newEnum("help", new CommandEnum("AthoraCoreHelp", "help"))
        });
        this.commandParameters.put("purse->get", new CommandParameter[]{
                CommandParameter.newEnum("purse", new CommandEnum("AthoraCorePurse", "purse")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCorePurseGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET)
        });
        this.commandParameters.put("purse->set", new CommandParameter[]{
                CommandParameter.newEnum("purse", new CommandEnum("AthoraCorePurse", "purse")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCorePurseSet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
        this.commandParameters.put("level->get", new CommandParameter[]{
                CommandParameter.newEnum("level", new CommandEnum("AthoraCoreLevel", "level")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCoreLevelGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
        this.commandParameters.put("level->set", new CommandParameter[]{
                CommandParameter.newEnum("level", new CommandEnum("AthoraCoreLevel", "level")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCoreLevelSet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
        this.commandParameters.put("ruhm->get", new CommandParameter[]{
                CommandParameter.newEnum("ruhm", new CommandEnum("AthoraCoreRuhm", "ruhm")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCoreRuhmGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
        this.commandParameters.put("ruhm->set", new CommandParameter[]{
                CommandParameter.newEnum("ruhm", new CommandEnum("AthoraCoreRuhm", "ruhm")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCoreRuhmSet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
        this.commandParameters.put("bankmoney->get", new CommandParameter[]{
                CommandParameter.newEnum("bankmoney", new CommandEnum("AthoraCoreBankmoney", "bankmoney")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCoreBankmoneyGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
        this.commandParameters.put("bankmoney->set", new CommandParameter[]{
                CommandParameter.newEnum("bankmoney", new CommandEnum("AthoraCoreBankmoney", "bankmoney")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCoreBankmoneySet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
        this.commandParameters.put("bankexperience->get", new CommandParameter[]{
                CommandParameter.newEnum("bankexperience", new CommandEnum("AthoraCoreBankexperience", "bankexperience")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCoreBankexperienceGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
        this.commandParameters.put("bankexperience->set", new CommandParameter[]{
                CommandParameter.newEnum("bankexperience", new CommandEnum("AthoraCoreBankexperience", "bankexperience")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCoreBankexperienceSet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
        this.commandParameters.put("bankstoragelevel->get", new CommandParameter[]{
                CommandParameter.newEnum("bankstoragelevel", new CommandEnum("AthoraCoreBankstoragelevel", "bankstoragelevel")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCoreBankstoragelevelGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
        this.commandParameters.put("bankstoragelevel->set", new CommandParameter[]{
                CommandParameter.newEnum("bankstoragelevel", new CommandEnum("AthoraCoreBankstoragelevel", "bankstoragelevel")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCoreBankstoragelevelSet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
        this.commandParameters.put("banksalarylevel->get", new CommandParameter[]{
                CommandParameter.newEnum("banksalarylevel", new CommandEnum("AthoraCoreBanksalarylevel", "banksalarylevel")),
                CommandParameter.newEnum("get", new CommandEnum("AthoraCoreBanksalarylevelGet", "get")),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
        this.commandParameters.put("banksalarylevel->set", new CommandParameter[]{
                CommandParameter.newEnum("banksalarylevel", new CommandEnum("AthoraCoreBanksalarylevel", "banksalarylevel")),
                CommandParameter.newEnum("set", new CommandEnum("AthoraCoreBanksalarylevelSet", "set")),
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("value", CommandParamType.INT)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("athora.core.api.commands")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast keine Berechtigung den Befehl auszuführen!");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze /athoracore help!");
            return false;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Vars.PREFIX + TextFormat.DARK_GRAY + "<---------- " + TextFormat.BLUE + "Befehle" + TextFormat.DARK_GRAY + " ---------->");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore purse [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore level [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore ruhm [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore bankmoney [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore bankexperience [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore bankstoragelevel [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.BLUE + "/athoracore banksalarylevel [get|set] playername <value>");
            sender.sendMessage(Vars.PREFIX + TextFormat.DARK_GRAY + "<------------------------------>");
            return true;
        }
        if (args.length >= 3) {

            if (args[1].equalsIgnoreCase("set") && args.length < 4 || !Helper.stringContainsItemFromList(args[1].toLowerCase(), new String[]{"get", "set"})) {
                sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze /athoracore help!");
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

            switch (args[0].toLowerCase()) {
                case "purse":
                    if (args[1].equalsIgnoreCase("get")) {
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat " + TextFormat.GOLD + String.format("%.2f", AthoraPlayer.getPurse(targetUUID)) + "$ " + TextFormat.GREEN + " in der Purse.");
                    } else {
                        AthoraPlayer.setPurse(targetUUID, Double.parseDouble(args[3]));
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat jetzt " + TextFormat.GOLD + String.format("%.2f", AthoraPlayer.getPurse(targetUUID)) + "$ " + TextFormat.GREEN + " in der Purse.");
                    }
                    break;
                case "level":
                    if (args[1].equalsIgnoreCase("get")) {
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " ist Level " + TextFormat.BLUE + AthoraPlayer.getLevel(targetUUID) + TextFormat.GREEN + ".");
                    } else {
                        AthoraPlayer.setLevel(targetUUID, Integer.parseInt(args[3]));
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " ist jetzt Level " + TextFormat.BLUE + AthoraPlayer.getLevel(targetUUID) + TextFormat.GREEN + ".");
                    }
                    break;
                case "ruhm":
                    if (args[1].equalsIgnoreCase("get")) {
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat " + TextFormat.BLUE + String.format("%.3f", AthoraPlayer.getRuhm(targetUUID)) + " Ruhm" + TextFormat.GREEN + ".");
                    } else {
                        AthoraPlayer.setRuhm(targetUUID, Double.parseDouble(args[3]));
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat jetzt " + TextFormat.BLUE + String.format("%.3f", AthoraPlayer.getRuhm(targetUUID)) + " Ruhm" + TextFormat.GREEN + ".");
                    }
                    break;
                case "bankmoney":
                    if (args[1].equalsIgnoreCase("get")) {
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat " + TextFormat.GOLD + String.format("%.2f", AthoraPlayer.getBankMoney(targetUUID)) + "$ " + TextFormat.GREEN + " auf der Bank.");
                    } else {
                        AthoraPlayer.setBankMoney(targetUUID, Double.parseDouble(args[3]));
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat jetzt " + TextFormat.GOLD + String.format("%.2f", AthoraPlayer.getBankMoney(targetUUID)) + "$ " + TextFormat.GREEN + " auf der Bank.");
                    }
                    break;
                case "bankexperience":
                    sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Work in progress");
                    break;
                case "bankstoragelevel":
                    if (args[1].equalsIgnoreCase("get")) {
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat Lagerplatz " + TextFormat.BLUE + "Stufe " + AthoraPlayer.getStorageLevel(targetUUID) + TextFormat.GREEN + ".");
                    } else {
                        AthoraPlayer.setStorageLevel(targetUUID, Integer.parseInt(args[3]));
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat jetzt Lagerplatz " + TextFormat.BLUE + "Stufe " + AthoraPlayer.getStorageLevel(targetUUID) + TextFormat.GREEN + ".");
                    }
                    break;
                case "banksalarylevel":
                    if (args[1].equalsIgnoreCase("get")) {
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat Zinsen " + TextFormat.BLUE + "Stufe " + AthoraPlayer.getSalaryLevel(targetUUID) + TextFormat.GREEN + ".");
                    } else {
                        AthoraPlayer.setSalaryLevel(targetUUID, Integer.parseInt(args[3]));
                        sender.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Der Spieler " + TextFormat.BLUE + args[2] + TextFormat.GREEN + " hat jetzt Zinsen " + TextFormat.BLUE + "Stufe " + AthoraPlayer.getSalaryLevel(targetUUID) + TextFormat.GREEN + ".");
                    }
                    break;
                default:
                    sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze /athoracore help!");
                    return false;
            }

        } else {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Benutze /athoracore help!");
            return false;
        }
        return true;
    }
}
