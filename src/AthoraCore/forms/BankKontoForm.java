package AthoraCore.forms;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.util.configs.BankConfig;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class BankKontoForm {

    public BankKontoForm(Player player) {
        SimpleForm form = new SimpleForm(TextFormat.GREEN + "Bank -> Konto")
                .setContent(
                        "Du hast " + TextFormat.GOLD + AthoraPlayer.getBankMoney(player) + "$" + TextFormat.RESET +
                                " von " + TextFormat.GREEN + BankConfig.getMoneyLimitByBankLevel(AthoraPlayer.getStorageLevel(player)) +
                                "$" + TextFormat.RESET + " in deiner Bank!\n\n" + "Dein " + TextFormat.BLUE + "Lagerplatz " +
                                TextFormat.RESET + "ist aktuell auf Level " +
                                TextFormat.BLUE + AthoraPlayer.getStorageLevel(player) + TextFormat.RESET + ".\n\n\n"
                )
                .addButton(TextFormat.GREEN + "Einzahlen")
                .addButton(TextFormat.RED + "Auszahlen")
                .addButton(TextFormat.BLUE + "Lagerplatz upgraden");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            int einzahlen = 0, auszahlen = 1, upgrade = 2;
            if (data == einzahlen) new BankEinzahlenForm(targetPlayer);
            if (data == auszahlen) new BankAuszahlenForm(targetPlayer);
            if (data == upgrade) new BankLagerplatzUpgradenForm(targetPlayer);
        });

    }
}
