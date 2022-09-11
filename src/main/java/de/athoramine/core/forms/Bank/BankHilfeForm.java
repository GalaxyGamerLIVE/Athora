package de.athoramine.core.forms.Bank;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class BankHilfeForm {

    public BankHilfeForm(Player player) {
        SimpleForm form = new SimpleForm("Hilfe")
                .setContent(
                        TextFormat.GOLD + "- Konto -\n\n" + TextFormat.RESET +
                                "Du willst nicht 50 Prozent von dein Geld verlieren wenn du stirbst? " +
                                "Dann Zahle dein Geld ein, dort ist es sicher!\n\nAusserdem bekommst du jeden Tag 1 Prozent Zinsen!\n\n" +
                                "Achte allerdings darauf, das du genug Lager Platz hast, vergrössere ihn um mehr Geld Lagern zu können.\n\n" +
                                TextFormat.GOLD + "- Gehalt -\n\n" + TextFormat.RESET +
                                "Jeder bekommt sein Gehalt, alle 30 min. doch wie viel?\n\n" +
                                "Erhöhe dein Gehalt sofern du die Voraussetzungen darfür hast!\n\n" +
                                TextFormat.GOLD + "- XP Lager -\n\n" + TextFormat.RESET +
                                "Du brauchst ein hohes Level aber willst deine XP nicht verlieren? Dann Lager sie ein!"
                );

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
        });

    }

}
