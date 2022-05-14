package AthoraCore.forms;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.util.Helper;
import AthoraCore.util.manager.LevelManager;
import AthoraCore.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.SimpleForm;

public class LevelUpForm {

    public LevelUpForm(Player player) {
        int playerLevel = AthoraPlayer.getLevel(player);

        SimpleForm form = new SimpleForm("§7[§2Level Up§7]")
                .setContent(
                        "<======[" + TextFormat.DARK_GREEN + "Nächstes Level" + TextFormat.RESET + "]======>\n\n" + TextFormat.RESET +
                                "[Dein Level]\n" +
                                TextFormat.DARK_GRAY + playerLevel + TextFormat.RESET + " -> " + TextFormat.BLUE + (playerLevel + 1) + "\n\n" + TextFormat.RESET +
                                "[Benötigter Ruhm]\n" +
                                TextFormat.BLUE + LevelManager.getRuhmRequirement(playerLevel + 1) + TextFormat.RESET + " (Dein Ruhm: " + TextFormat.RED + AthoraPlayer.getRuhm(player) + TextFormat.RESET + ")\n\n" +
                                "[Kosten]\n" +
                                TextFormat.GOLD + LevelManager.getMoneyRequirement(playerLevel + 1) + "$" + TextFormat.RESET + " (Du hast " + TextFormat.GOLD + AthoraPlayer.getPurse(player) + "$" + TextFormat.RESET + " dabei)"
                )
                .addButton(TextFormat.DARK_GREEN + "Level upgraden\n" + TextFormat.RED + playerLevel + TextFormat.BLACK + " -> " + TextFormat.RED + (playerLevel + 1))
                .addButton(TextFormat.RED + "Abbrechen");

        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            int upgrade = 0, cancel = 1;
            if (data == cancel) return;
            if (data == upgrade) {
                if (AthoraPlayer.getPurse(targetPlayer) >= LevelManager.getMoneyRequirement(AthoraPlayer.getLevel(targetPlayer) + 1) && AthoraPlayer.getRuhm(targetPlayer) >= LevelManager.getRuhmRequirement(AthoraPlayer.getLevel(targetPlayer) + 1)) {
                    AthoraPlayer.setPurse(targetPlayer, AthoraPlayer.getPurse(targetPlayer) - LevelManager.getMoneyRequirement(AthoraPlayer.getLevel(targetPlayer) + 1));
                    AthoraPlayer.setLevel(targetPlayer, AthoraPlayer.getLevel(targetPlayer) + 1);
                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du bist jetzt Level " + TextFormat.BLUE + AthoraPlayer.getLevel(targetPlayer) + TextFormat.GREEN + ".");
                    Helper.createFireworkParticle(targetPlayer, DyeColor.CYAN);
                } else {
                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du erfüllst nicht alle Voraussetzungen!");
                }
            }
        });
    }

}
