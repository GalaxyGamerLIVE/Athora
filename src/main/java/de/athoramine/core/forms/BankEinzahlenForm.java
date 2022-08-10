package de.athoramine.core.forms;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.configs.BankConfig;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.CustomForm;

public class BankEinzahlenForm {

    public BankEinzahlenForm(Player player) {
        double maxMoney;
        double currentMoney = AthoraPlayer.getPurse(player);
        double currentBankMoney = AthoraPlayer.getBankMoney(player);
        int maxMoneyByBankLevel = BankConfig.getMoneyLimitByBankLevel(AthoraPlayer.getStorageLevel(player));

        if (currentMoney < 1) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast kein Geld dabei!");
            return;
        }

        if (currentBankMoney >= maxMoneyByBankLevel) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst deinen Lagerplatz upgraden um mehr einzahlen zu können!");
            return;
        }

        if ((currentMoney + currentBankMoney) > maxMoneyByBankLevel) {
            maxMoney = currentMoney - ((currentMoney + currentBankMoney) - maxMoneyByBankLevel);
        } else {
            maxMoney = currentMoney;
        }

        CustomForm form = new CustomForm(TextFormat.GREEN + "Bank -> Konto -> Einzahlen")
                .addLabel("Du hast aktuell " + TextFormat.GOLD + currentMoney + "$" + TextFormat.RESET + " dabei!")
                .addSlider("Einzahlen", 1, (int) maxMoney, 1, 1);
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == null) return;

            int transferMoney = Math.round((Float) targetForm.getResponse().getResponse(1));

            AthoraPlayer.setPurse(targetPlayer, (currentMoney - transferMoney));
            AthoraPlayer.setBankMoney(targetPlayer, (currentBankMoney + transferMoney));
            targetPlayer.sendMessage(TextFormat.GREEN + "Du hast erfolgreich " + TextFormat.GOLD + transferMoney + "$" + TextFormat.GREEN + " eingezahtl!");
            Helper.playSound("note.harp", targetPlayer, 1, 1);
        });
    }

}
