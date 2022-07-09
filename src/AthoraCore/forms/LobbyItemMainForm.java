package AthoraCore.forms;

import AthoraCore.util.manager.LobbyItemManager;
import AthoraCore.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import ru.nukkitx.forms.elements.ImageType;
import ru.nukkitx.forms.elements.SimpleForm;

public class LobbyItemMainForm {

    public LobbyItemMainForm(Player player) {
        SimpleForm form = new SimpleForm("§o- Lobby Item -");

        if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
            form.addButton("§l§3- Warps -", ImageType.PATH, "textures/blocks/beacon");
            form.addButton("§l§a$ §2Gehalt §a$\n§r§o§8(Upgrade bei der Bank)", ImageType.PATH, "textures/items/gold_ingot");
            form.addButton("§l§9Level Up\n§r§o§8(Schalte neue Sachen frei)", ImageType.PATH, "textures/items/experience_bottle");
            form.addButton("§l§5- Secrets -", ImageType.PATH, "textures/items/ender_eye");
            form.addButton("§l§c- Hilfe -", ImageType.PATH, "textures/blocks/redstone_block");
        } else {
            form.addButton("§l§3- Warps -", ImageType.PATH, "textures/blocks/beacon");
            form.addButton("§l§a$ §2Gehalt §a$\n§r§o§8(Upgrade bei der Bank)", ImageType.PATH, "textures/items/gold_ingot");
            form.addButton("§l§9Level Up\n§r§o§8(Schalte neue Sachen frei)", ImageType.PATH, "textures/items/experience_bottle");
            form.addButton("§l§a- Login Bonus -", ImageType.PATH, "textures/blocks/chest_front");
            form.addButton("§l§6- Freunde - ", ImageType.PATH, "textures/items/name_tag");
            form.addButton("§l§5- Secrets -", ImageType.PATH, "textures/items/ender_eye");
            form.addButton("§l§c- Hilfe -", ImageType.PATH, "textures/blocks/redstone_block");
        }

        form.send(player, (targetPlayer, targetForm, data) -> {
            LobbyItemManager.openedForms.remove(player.getUniqueId());
            if (data == -1) return;
            if (ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
                switch (data) {
                    case 0: //warps
                        new LobbyItemWarpsForm(targetPlayer);
                        break;
                    case 1: //gehalt
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "gehalt");
                        break;
                    case 2: //levelup
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "levelup");
                        break;
                    case 3: //secrets
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "secrets");
                        break;
                    case 4: //help
                        new LobbyItemHelpForm(targetPlayer);
                        break;
                }
            } else {
                switch (data) {
                    case 0: //warps
                        new LobbyItemWarpsForm(targetPlayer);
                        break;
                    case 1: //gehalt
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "gehalt");
                        break;
                    case 2: //levelup
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "levelup");
                        break;
                    case 3: //login bonus
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "login");
                        break;
                    case 4: //friends
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "friend");
                        break;
                    case 5: //secrets
                        Server.getInstance().getCommandMap().dispatch(targetPlayer, "secrets");
                        break;
                    case 6: //help
                        new LobbyItemHelpForm(targetPlayer);
                        break;
                }
            }
        });
    }

}
