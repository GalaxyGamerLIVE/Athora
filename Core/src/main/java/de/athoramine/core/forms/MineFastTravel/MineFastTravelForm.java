package de.athoramine.core.forms.MineFastTravel;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.MineManager;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.ImageType;
import ru.nukkitx.forms.elements.SimpleForm;

public class MineFastTravelForm {

    public MineFastTravelForm(Player player) {
        SimpleForm form = new SimpleForm(TextFormat.DARK_GRAY + "§l[" + TextFormat.DARK_GREEN + "Mine-Schnellreise" + TextFormat.DARK_GRAY + "]")
                .addButton(TextFormat.BLUE + "§lMine 1\n" + TextFormat.RESET + TextFormat.RED + "ab Level " + MineManager.getMineRequirements(1) + "!", ImageType.PATH, "textures/items/wood_pickaxe")
                .addButton(TextFormat.BLUE + "§lMine 2\n" + TextFormat.RESET + TextFormat.RED + "ab Level " + MineManager.getMineRequirements(2) + "!", ImageType.PATH, "textures/items/stone_pickaxe")
                .addButton(TextFormat.BLUE + "§lMine 3\n" + TextFormat.RESET + TextFormat.RED + "ab Level " + MineManager.getMineRequirements(3) + "!", ImageType.PATH, "textures/items/iron_pickaxe")
                .addButton(TextFormat.BLUE + "§lMine 4\n" + TextFormat.RESET + TextFormat.RED + "ab Level " + MineManager.getMineRequirements(4) + "!", ImageType.PATH, "textures/items/gold_pickaxe")
                .addButton(TextFormat.BLUE + "§lMine 5\n" + TextFormat.RESET + TextFormat.RED + "ab Level " + MineManager.getMineRequirements(5) + "!", ImageType.PATH, "textures/items/diamond_pickaxe")
                .addButton(TextFormat.BLUE + "§lMine 6\n" + TextFormat.RESET + TextFormat.RED + "ab Level " + MineManager.getMineRequirements(6) + "!", ImageType.PATH, "textures/items/netherite_pickaxe");
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            int mine = data + 1;
            if (AthoraPlayer.getLevel(targetPlayer) >= MineManager.getMineRequirements(mine)) {
                targetPlayer.teleport(MineManager.getMineSpawnpoint(mine, "start"));
                targetPlayer.sendTitle(TextFormat.GREEN + "Mine " + mine);
            } else {
                targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du brauchst Level " + TextFormat.BLUE + MineManager.getMineRequirements(mine) + TextFormat.RED + " um in die Mine zu kommen!");
            }
        });
    }

}
