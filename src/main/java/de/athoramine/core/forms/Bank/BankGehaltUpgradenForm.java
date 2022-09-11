package de.athoramine.core.forms.Bank;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.configs.BankConfig;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class BankGehaltUpgradenForm {

    public BankGehaltUpgradenForm(Player player) {
        int currentSalaryLevel = AthoraPlayer.getSalaryLevel(player);
        int currentSalaryAmount = BankConfig.getSalaryAmountBySalaryLevel(currentSalaryLevel);
        int currentPlayerLevel = AthoraPlayer.getLevel(player);

        if (BankConfig.getRequiredRankBySalaryLevel(currentSalaryLevel + 1) != 0) {
            String headline = "<======[" + TextFormat.BLUE + "Nächstes Upgrade" + TextFormat.RESET + "]======>\n\n" + TextFormat.RESET;
            String salary = "[Gehalt]\n" + TextFormat.RESET + TextFormat.DARK_GRAY + currentSalaryAmount + TextFormat.RESET + " -> " + TextFormat.BLUE + BankConfig.getSalaryAmountBySalaryLevel(currentSalaryLevel + 1) + "\n\n" + TextFormat.RESET;
            String rank = "[Benötigter Rang]\n" + TextFormat.BLUE + BankConfig.getRequiredRankBySalaryLevel(currentSalaryLevel + 1) + TextFormat.RESET + " (Dein Rang: " + TextFormat.RED + currentPlayerLevel + TextFormat.RESET + ")\n\n";
            String costs = "[Kosten]\n" + TextFormat.GOLD + BankConfig.getUpgradesCostsBySalaryLevel(currentSalaryLevel + 1) + "$" + TextFormat.RESET + " (Du hast " + TextFormat.GOLD + AthoraPlayer.getBankMoney(player) + "$" + TextFormat.RESET + " auf der Bank)";
            SimpleForm form = new SimpleForm(TextFormat.GREEN + "Bank -> Gehalt -> Upgraden")
                    .setContent(headline + salary + rank + costs + "\n\n")
                    .addButton(TextFormat.DARK_BLUE + "Gehalt Upgraden\n" + TextFormat.RED + currentSalaryLevel + TextFormat.BLACK + " -> " + TextFormat.RED + (currentSalaryLevel + 1))
                    .addButton(TextFormat.RED + "Zurück");
            form.send(player, (targetPlayer, targetForm, data) -> {
                if (data == -1) return;
                int upgrade = 0, back = 1;
                if (data == back) new BankGehaltForm(targetPlayer);
                if (data == upgrade) {
                    double currentMoney = AthoraPlayer.getBankMoney(targetPlayer);
                    int requiredMoney = BankConfig.getUpgradesCostsBySalaryLevel(currentSalaryLevel + 1);
                    if (currentMoney >= requiredMoney) {
                        int requiredRank = BankConfig.getRequiredRankBySalaryLevel(currentSalaryLevel + 1);
                        if (currentPlayerLevel >= requiredRank) {
                            AthoraPlayer.setBankMoney(targetPlayer, (currentMoney - requiredMoney));
                            AthoraPlayer.setSalaryLevel(targetPlayer, (currentSalaryLevel + 1));
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich dein " + TextFormat.BLUE +  "Gehalt" + TextFormat.GREEN +  " auf Stufe " + TextFormat.BLUE + (currentSalaryLevel + 1) + TextFormat.GREEN + " geupgraded!");
                            Helper.playSound("random.levelup", targetPlayer, 1, 1);
                        } else {
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst Rang " + requiredRank + " sein um upgraden zu können!");
                        }
                    } else {
                        targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Dir fehlen noch " + TextFormat.GOLD + (requiredMoney - currentMoney) + "$" + TextFormat.RED + " und du musst das Geld auf der Bank haben!");
                    }
                }
            });
        } else {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast bereits alle Upgrades freigeschalten!");
        }

    }

}
