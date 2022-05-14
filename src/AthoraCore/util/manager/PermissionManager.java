package AthoraCore.util.manager;

import cn.nukkit.Player;
import net.luckperms.api.LuckPermsProvider;

public class PermissionManager {

    public static String getGroup(Player player) {
        String group = "";
        try {
            group = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
            group = group.substring(0, 1).toUpperCase() + group.substring(1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return group;
    }

    public static String getColorCodeFromGroup(Player player) {
        String group = getGroup(player);
        String color = "";
        switch (group) {
            case "Owner":
                color = "§4";
                break;
            case "Admin":
                color = "§4";
                break;
            case "Developer":
                color = "§b";
                break;
            case "Moderator":
                color = "§c";
                break;
            case "Builder":
                color = "§3";
                break;
            case "Supporter":
                color = "§a";
                break;
            default:
                color = "§f";
        }
        return color;
    }

}
