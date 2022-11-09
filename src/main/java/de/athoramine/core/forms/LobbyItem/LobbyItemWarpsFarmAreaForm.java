package de.athoramine.core.forms.LobbyItem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import ru.nukkitx.forms.elements.ImageType;
import ru.nukkitx.forms.elements.SimpleForm;

public class LobbyItemWarpsFarmAreaForm {

    public LobbyItemWarpsFarmAreaForm(Player player) {
        SimpleForm form = new SimpleForm("§o- Farm gebiet -");
        form.addButton("§l§2 - Start -");
        form.addButton("Dorf", ImageType.PATH, "textures/blocks/hay_block_side");
        form.addButton("Eichenwald", ImageType.PATH, "textures/blocks/log_oak");
        form.addButton("Birkenwald", ImageType.PATH, "textures/blocks/log_birch");
        form.addButton("Jungle", ImageType.PATH, "textures/blocks/log_jungle");
        form.addButton("Fichtenwald", ImageType.PATH, "textures/blocks/log_spruce");
        form.addButton("Schwarzeichenwald", ImageType.PATH, "textures/blocks/log_big_oak");
        form.addButton("Gebirge", ImageType.PATH, "textures/blocks/stone");
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            switch (data) {
                case 0:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp farmgebiet");
                    return;
                case 1:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp dorf");
                    return;
                case 2:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp eichenwald");
                    return;
                case 3:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp birkenwald");
                    return;
                case 4:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp jungle");
                    return;
                case 5:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp Fichtenwald");
                    return;
                case 6:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp schwarzeichenwald");
                    return;
                case 7:
                    Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp gebirge");
                    return;
            }
        });
    }

}
