package AthoraCore.forms;

import AthoraCore.util.Vars;
import AthoraCore.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.ImageType;
import ru.nukkitx.forms.elements.SimpleForm;

public class LobbyItemWarpsForm {

    public LobbyItemWarpsForm(Player player) {
        SimpleForm form = new SimpleForm("§o- Warps -");
        form.addButton("§l§1 - Hub -", ImageType.PATH, "textures/blocks/beacon");
        if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
            form.addButton("§l§9- Lobby -", ImageType.PATH, "textures/blocks/bedrock");
        } else {
            form.addButton("§l§9- Plots -", ImageType.PATH, "textures/blocks/bedrock");
        }
        form.addButton("§l§6- Mine -", ImageType.PATH, "textures/items/iron_pickaxe");
        form.addButton("§l§a- Farm §2Gebiet -", ImageType.PATH, "textures/blocks/sapling_oak");
        form.addButton("§l§a$ §2Shop §a$", ImageType.PATH, "textures/blocks/emerald_block");
        if (!ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
            form.addButton("§l§g$ §6Bank §g$", ImageType.PATH, "textures/blocks/gold_block");
        }
        form.addButton("§l§1- Spawn - ");
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;
            if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
                switch (data) {
                    case 0: // Hub
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "spawn");
                        return;
                    case 1: // Lobby
                        ServerManager.sendPlayerToServer(targetPlayer, ServerManager.LOBBY_SERVER);
                        return;
                    case 2: // Mine
                    case 3: // Farm
                        targetPlayer.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst dich für den Warp in der Lobby befinden!");
                        return;
                    case 4: // Shop
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp shop");
                        return;
                    case 5: // Hub Bottom
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "hub");
                        return;
                }
            } else {
                switch (data) {
                    case 0: // Hub
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "spawn");
                        return;
                    case 1: // Plots
                        ServerManager.sendPlayerToServer(targetPlayer, ServerManager.PLOT_SERVER);
                        return;
                    case 2: // Mine
                        Server.getInstance().getCommandMap().dispatch(Server.getInstance().getConsoleSender(), "minefasttravel " + targetPlayer.getName());
                        return;
                    case 3: // Farm
                        new LobbyItemWarpsFarmAreaForm(targetPlayer);
                        return;
                    case 4: // Shop
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp shop");
                        return;
                    case 5: // Bank
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "warp bank");
                        return;
                    case 6: // Hub Bottom
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "hub");
                        return;
                }
            }
        });
    }

}
