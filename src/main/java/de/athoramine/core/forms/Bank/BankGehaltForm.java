package de.athoramine.core.forms.Bank;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.configs.BankConfig;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class BankGehaltForm {

    public BankGehaltForm(Player player) {
        SimpleForm form = new SimpleForm(TextFormat.GREEN + "Bank -> Gehalt")
                .setContent(
                        "Du bekommst aktuell alle 30 Minuten " + TextFormat.GOLD +
                                BankConfig.getSalaryAmountBySalaryLevel(AthoraPlayer.getSalaryLevel(player)) + "$" +
                                TextFormat.RESET + " Gehalt!\n\nDein Gehalt ist aktuell auf Stufe " +
                                TextFormat.BLUE + AthoraPlayer.getSalaryLevel(player) + TextFormat.RESET + ".\n\n\n"
                )
                .addButton(TextFormat.BLUE + "Gehalt upgraden");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            int upgrade = 0;
            if (data == upgrade) new BankGehaltUpgradenForm(player);
        });

    }

}
