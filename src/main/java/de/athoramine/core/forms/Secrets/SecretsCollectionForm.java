package de.athoramine.core.forms.Secrets;

import de.athoramine.core.util.manager.SecretsManager;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class SecretsCollectionForm {

    public SecretsCollectionForm(Player player) {
        SimpleForm form = new SimpleForm(TextFormat.DARK_GRAY + "§l[" + TextFormat.DARK_BLUE + "Secrets Sammlung" + TextFormat.DARK_GRAY + "]")
                .setContent(getCollection(player));

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
        });
    }

    private String getCollection(Player player) {
        StringBuilder content = new StringBuilder();

        int count = 1;
        for (Block secret : SecretsManager.secrets.keySet()) {
            StringBuilder line = new StringBuilder();

            line.append(TextFormat.GOLD + "" + count + ": ");

            if (SecretsManager.hasPlayerFoundSecret(player, secret)) {
                line.append(TextFormat.GREEN + "Gefunden ");
                line.append(TextFormat.BLUE + "[Platz " + SecretsManager.getPlayerPositionOnSecret(player, secret) + "]");
                line.append(TextFormat.GOLD + ", ");
            } else {
                line.append(TextFormat.RED + "Nicht Gefunden");
                line.append(TextFormat.GOLD + ", ");
            }

            line.append(TextFormat.GREEN + "" + SecretsManager.getSecretDiscovers(secret) + " mal gefunden\n");

            content.append(line);
            count++;
        }

        return content.toString();
    }

}
