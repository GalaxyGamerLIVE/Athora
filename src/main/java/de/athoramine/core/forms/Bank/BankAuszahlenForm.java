package de.athoramine.core.forms.Bank;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.CustomForm;

public class BankAuszahlenForm {

    public BankAuszahlenForm(Player player) {
        double bankMoney = AthoraPlayer.getBankMoney(player);
        if (bankMoney < 1) {
            player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast aktuell kein Geld auf der Bank!");
            return;
        }

        CustomForm form = new CustomForm(TextFormat.GREEN + "Bank -> Konto -> Auszahlen")
                .addLabel("Du hast aktuell " + TextFormat.GOLD + bankMoney + "$" + TextFormat.RESET + " auf der Bank!")
                .addSlider("Auszahlen", 1, (int) bankMoney, 1, 1);
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == null) return;

            int transferMoney = Math.round((Float) targetForm.getResponse().getResponse(1));

            AthoraPlayer.setPurse(targetPlayer, (AthoraPlayer.getPurse(player) + transferMoney));
            AthoraPlayer.setBankMoney(targetPlayer, (AthoraPlayer.getBankMoney(player) - transferMoney));
            targetPlayer.sendMessage(TextFormat.GREEN + "Du hast erfolgreich " + TextFormat.GOLD + transferMoney + "$" + TextFormat.GREEN + " ausgezahlt!");
            Helper.playSound("note.harp", targetPlayer, 1,1);
        });
    }

}
