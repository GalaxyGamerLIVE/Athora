package AthoraCore.forms;

import AthoraCore.util.Vars;
import AthoraCore.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.ImageType;
import ru.nukkitx.forms.elements.SimpleForm;

public class BankMainForm {

    public BankMainForm(Player player) {
        SimpleForm form = new SimpleForm(Vars.PREFIX);
        form.addButton(TextFormat.DARK_GRAY + "[Konto]", ImageType.PATH, "textures/items/gold_ingot");
        form.addButton(TextFormat.DARK_GRAY + "[Gehalt]", ImageType.PATH, "textures/items/book_writable");

        if (!ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
            form.addButton(TextFormat.DARK_GRAY + "[XP Lager]", ImageType.PATH, "textures/items/experience_bottle");
        }

        form.addButton(TextFormat.DARK_GRAY + "[" + TextFormat.RED + "Hilfe" + TextFormat.DARK_GRAY + "]", ImageType.URL, Vars.URL_HILFE);

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;

            int konto = 0, gehalt = 1, xpLager = 2, hilfe = 3;

            if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
                hilfe = 2;
                xpLager = 999999;
            }

            if (data == konto) new BankKontoForm(targetPlayer);
            if (data == gehalt) new BankGehaltForm(targetPlayer);

            if (data == xpLager) Server.getInstance().dispatchCommand(targetPlayer, "xpbank");

            if (data == hilfe) new BankHilfeForm(targetPlayer);
        });
    }
}
