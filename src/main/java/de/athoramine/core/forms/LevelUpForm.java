package de.athoramine.core.forms;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.manager.LevelManager;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.PermissionManager;
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
                                TextFormat.BLUE + LevelManager.getRuhmRequirement(playerLevel + 1) + TextFormat.RESET + " (Dein Ruhm: " + TextFormat.RED + String.format("%.2f", AthoraPlayer.getRuhm(player)) + TextFormat.RESET + ")\n\n" +
                                "[Kosten]\n" +
                                TextFormat.GOLD + LevelManager.getMoneyRequirement(playerLevel + 1) + "$" + TextFormat.RESET + " (Du hast " + TextFormat.GOLD + String.format("%.2f", AthoraPlayer.getPurse(player)) + "$" + TextFormat.RESET + " dabei)"
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
//                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du bist jetzt Level " + TextFormat.BLUE + AthoraPlayer.getLevel(targetPlayer) + TextFormat.GREEN + ".");

                    StringBuilder message = new StringBuilder();
                    message.append("§7<===== §l§9Level Up!§r§l§7 =====>\n\n§8- §6Du bist jetzt Level§9 " + newLevel + "§r\n");

                    Helper.createFireworkParticle(targetPlayer, DyeColor.CYAN);

                    int levelRewardLevel = LevelManager.getLevelRewardLevel(newLevel);
                    if (LevelManager.levelConfig.exists("level_rewards.level_" + levelRewardLevel)) {
                        int commonCount = LevelManager.levelConfig.getInt("level_rewards.level_" + levelRewardLevel + ".common");
                        int rareCount = LevelManager.levelConfig.getInt("level_rewards.level_" + levelRewardLevel + ".rare");
                        int epicCount = LevelManager.levelConfig.getInt("level_rewards.level_" + levelRewardLevel + ".epic");
                        int legendaryCount = LevelManager.levelConfig.getInt("level_rewards.level_" + levelRewardLevel + ".legendary");
                        if (commonCount > 0) {
                            for (int i = 0; i < commonCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(Server.getInstance().getConsoleSender(), "key common " + targetPlayer.getName());
                            }
                            //targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.WHITE + commonCount + "x Common Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                            message.append("§8- §eDu erhälst: " + TextFormat.WHITE + commonCount + "x Common Key\n");
                        }
                        if (rareCount > 0) {
                            for (int i = 0; i < rareCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(Server.getInstance().getConsoleSender(), "key rare " + targetPlayer.getName());
                            }
                            message.append("§8- §eDu erhälst: " + TextFormat.AQUA + rareCount + "x Rare Key\n");
//                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.AQUA + rareCount + "x Rare Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                        if (epicCount > 0) {
                            for (int i = 0; i < epicCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(Server.getInstance().getConsoleSender(), "key epic " + targetPlayer.getName());
                            }
                            message.append("§8- §eDu erhälst: " + TextFormat.DARK_PURPLE + epicCount + "x Epic Key\n");
//                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.DARK_PURPLE + epicCount + "x Epic Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                        if (legendaryCount > 0) {
                            for (int i = 0; i < legendaryCount; i++) {
                                Server.getInstance().getCommandMap().dispatch(Server.getInstance().getConsoleSender(), "key legendary " + targetPlayer.getName());
                            }
                            message.append("§8- §eDu erhälst: " + TextFormat.GOLD + legendaryCount + "x Legendary Key\n");
//                            targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast als Belohnung " + TextFormat.GOLD + legendaryCount + "x Legendary Key " + TextFormat.GREEN + "für das Chest Opening bekommen!");
                        }
                    }

                    message.append("§8\n------------------------------");
                    player.sendMessage(message.toString());

                    if (newLevel == 15) {
                        PermissionManager.addPermission(targetPlayer.getUniqueId(), "futureplots.command.merge");
                        targetPlayer.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du kannst ab jetzt auf dem Plot Server den " + TextFormat.AQUA + "/plot merge " + TextFormat.GREEN + "Befehl benutzen!");
                    }

                } else {
                    targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du erfüllst nicht alle Voraussetzungen!");
                }
            }
        });
    }

}
