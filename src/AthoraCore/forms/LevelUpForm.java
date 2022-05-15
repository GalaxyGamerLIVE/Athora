package AthoraCore.forms;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.util.Helper;
import AthoraCore.util.manager.LevelManager;
import AthoraCore.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.Server;
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
                    int newLevel = AthoraPlayer.getLevel(targetPlayer) + 1;
                    AthoraPlayer.setLevel(targetPlayer, newLevel);
                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du bist jetzt Level " + TextFormat.BLUE + AthoraPlayer.getLevel(targetPlayer) + TextFormat.GREEN + ".");
                    Helper.createFireworkParticle(targetPlayer, DyeColor.CYAN);

                    if (LevelManager.levelConfig.exists("level_rewards.level_" + newLevel)) {
                        int commonCount = LevelManager.levelConfig.getInt("level_rewards.level_" + newLevel + ".common");
                        int rareCount = LevelManager.levelConfig.getInt("level_rewards.level_" + newLevel + ".rare");
                        int epicCount = LevelManager.levelConfig.getInt("level_rewards.level_" + newLevel + ".epic");
                        int legendaryCount = LevelManager.levelConfig.getInt("level_rewards.level_" + newLevel + ".legendary");
                        if (commonCount > 0) {
                            for (int i = 0; i < commonCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(player, "key common " + targetPlayer.getName());
                            }
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.WHITE + commonCount + "x Common Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                        if (rareCount > 0) {
                            for (int i = 0; i < rareCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(player, "key rare " + targetPlayer.getName());
                            }
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.DARK_BLUE + rareCount + "x Rare Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                        if (epicCount > 0) {
                            for (int i = 0; i < epicCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(player, "key epic " + targetPlayer.getName());
                            }
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.DARK_PURPLE + epicCount + "x Epic Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                        if (legendaryCount > 0) {
                            for (int i = 0; i < legendaryCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(player, "key legendary " + targetPlayer.getName());
                            }
                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.GOLD + legendaryCount + "x Legendary Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                    }

                } else {
                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du erfüllst nicht alle Voraussetzungen!");
                }
            }
        });
    }

}
