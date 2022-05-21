package AthoraCore.util.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.TransferPacket;

public class ServerManager {

    public static final String LOBBY_SERVER = "lobby1";
    public static final String PLOT_SERVER = "plots1";
    public static final String DEV_SERVER = "dev-server";

    public static void sendPlayerToServer(Player player, String servername) {
        TransferPacket packet = new TransferPacket();
        packet.address = servername;
        player.dataPacket(packet);
    }

    public static String getCurrentServer() {
        return Server.getInstance().getMotd().toLowerCase();
    }


}
