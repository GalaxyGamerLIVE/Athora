package de.athoramine.core.forms;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.configs.BankConfig;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class BankLagerplatzUpgradenForm {

    public BankLagerplatzUpgradenForm(Player player) {
        int currentStorageLevel = AthoraPlayer.getStorageLevel(player);
        int currentMoneyLimit = BankConfig.getMoneyLimitByBankLevel(currentStorageLevel);
        int currentPlayerLevel = AthoraPlayer.getLevel(player);

        if (BankConfig.getRequiredRankByBankLevel(currentStorageLevel + 1) != 0) {
            String headline = "<======[" + TextFormat.BLUE + "Nächstes Upgrade" + TextFormat.RESET + "]======>\n\n" + TextFormat.RESET;
            String lagerplatz = "[Lagerplatz]\n" + TextFormat.RESET + TextFormat.DARK_GRAY + currentMoneyLimit + TextFormat.RESET + " -> " + TextFormat.BLUE + BankConfig.getMoneyLimitByBankLevel(currentStorageLevel + 1) + "\n\n" + TextFormat.RESET;
            String rang = "[Benötigter Rang]\n" + TextFormat.BLUE + BankConfig.getRequiredRankByBankLevel(currentStorageLevel + 1) + TextFormat.RESET + " (Dein Rang: " + TextFormat.RED + currentPlayerLevel + TextFormat.RESET + ")\n\n";
            String kosten = "[Kosten]\n" + TextFormat.GOLD + BankConfig.getUpgradesCostsByBankLevel(currentStorageLevel + 1) + "$" + TextFormat.RESET + " (Du hast " + TextFormat.GOLD + AthoraPlayer.getBankMoney(player) + "$" + TextFormat.RESET + " auf der Bank)";
            SimpleForm form = new SimpleForm(TextFormat.GREEN + "Bank -> Konto -> Lagerplatz")
                    .setContent(headline + lagerplatz + rang + kosten + "\n\n")
                    .addButton(TextFormat.DARK_BLUE + "Lagerplatz Upgraden\n" + TextFormat.RED + currentStorageLevel + TextFormat.BLACK + " -> " + TextFormat.RED + (currentStorageLevel + 1))
                    .addButton(TextFormat.RED + "Zurück");
            form.send(player, (targetPlayer, targetForm, data) -> {
                if (data == -1) return;
                int upgrade = 0, back = 1;
                if (data == back) new BankKontoForm(targetPlayer);
                if (data == upgrade) {
                    double currentMoney = AthoraPlayer.getBankMoney(targetPlayer);
                    int requiredMoney = BankConfig.getUpgradesCostsByBankLevel(currentStorageLevel + 1);
                    if (currentMoney >= requiredMoney) {
                        int requiredRank = BankConfig.getRequiredRankByBankLevel(currentStorageLevel + 1);
                        if (currentPlayerLevel >= requiredRank) {
                            AthoraPlayer.setBankMoney(targetPlayer, (currentMoney - requiredMoney));
                            AthoraPlayer.setStorageLevel(targetPlayer, (currentStorageLevel + 1));
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich dein " + TextFormat.BLUE +  "Lagerplatz" + TextFormat.GREEN +  " auf " + TextFormat.BLUE + BankConfig.getMoneyLimitByBankLevel(currentStorageLevel + 1) + TextFormat.GREEN + " erweitert!");
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
