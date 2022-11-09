package de.athoramine.core.commands;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.configs.BankConfig;
import de.athoramine.core.util.manager.SalaryManager;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.utils.TextFormat;

import java.util.concurrent.TimeUnit;

public class GehaltCommand extends PluginCommand<Main> {

    public GehaltCommand(Main owner) {
        super("gehalt", owner);
        this.setDescription("Hole dir mit den Befehl dein Gehalt ab!");
        this.commandParameters.clear();

    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Vars.PREFIX + TextFormat.RED + "Es können nur Spieler diesen Befehl ausführen!");
            return false;
        }
        Player player = (Player) sender;
        if (SalaryManager.reachedMinSalaryTime(player)) {
            int salaryAmount = BankConfig.getSalaryAmountBySalaryLevel(AthoraPlayer.getSalaryLevel(player));
            AthoraPlayer.setPurse(player, (AthoraPlayer.getPurse(player) + salaryAmount));
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast dir erfolgreich dein Gehalt in Höhe von " + TextFormat.GOLD + salaryAmount + "$ " + TextFormat.GREEN + "abgeholt!");
            Helper.playSound("random.levelup", player, 0.3f, 0.8f);
            SalaryManager.resetSalaryStats(player);
            SalaryManager.setPlayerToNotSalaryFinished(player);
        } else {
            String remainingTime = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((SalaryManager.MIN_SALARY_TIME - SalaryManager.getTotalPlaytime(player))));
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du kannst dein Gehalt erst in " + TextFormat.BLUE + remainingTime + " Minuten " + TextFormat.RED + "abholen!");
        }
        return true;
    }
}
