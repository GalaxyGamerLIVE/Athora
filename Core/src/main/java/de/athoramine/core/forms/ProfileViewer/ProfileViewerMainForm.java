package de.athoramine.core.forms.ProfileViewer;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class ProfileViewerMainForm {

    public ProfileViewerMainForm(Player player) {

        SimpleForm form = new SimpleForm(TextFormat.GOLD + "§lSpielerstatistiken");
        form.addButton("Eigene Statistiken");
        form.addButton("Spieler suchen");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            if (data == 0) {
                // TODO open profile viewer for target player
            } else if (data == 1) {
                // TODO open search player form
            }
        });

    }

}
