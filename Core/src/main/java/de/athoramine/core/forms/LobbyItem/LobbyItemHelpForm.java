package de.athoramine.core.forms.LobbyItem;

import cn.nukkit.Player;
import ru.nukkitx.forms.elements.SimpleForm;

public class LobbyItemHelpForm {

    public LobbyItemHelpForm(Player player) {
        SimpleForm form = new SimpleForm("§cHilfe");
        form.setContent(
                "§fFür Support: §5discord/rdh5Edjbm\n\n" +
                "§6Wie verdiene ich Geld?\n" +
                "§fDie erste Anlaufställe ist die Mine, du kannst aber auch durch Sammeln, Farmen, Zinsen und Gehalt an dein Geld Kommen! (tipps am Shop)\n\n" +
                "§6Was ist Ruhm und wie kriege ich ihn?\n" +
                "§fRuhm ist das was du in anderen Spielen als XP kennst, du verdienst es durch alles was du tust, du brauchst es um auf zu leveln! Außerdem zeigt es genau an, wie aktiv du schon auf dem Server gespielt hast!\n\n" +
                "§6Wie Vote ich?§r\n" +
                "1. klicke auf dem Link in Unserem Discord 2. Trage dein Unsername ein 3. gebe ingame /vote ein!\n\n" +
                "(Link: §3https://minecraftpocket-servers.com/server/118281/vote§r)"
        );
        form.send(player);
    }

}
