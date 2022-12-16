package de.athoramine.core.listener;

import cn.nukkit.network.protocol.ResourcePackStackPacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import de.athoramine.core.main.Main;

public class ListenDataPacket implements Listener {

    private Main plugin;

    public ListenDataPacket(Main main) {
        plugin = main;
    }

    @EventHandler
    public void onDataPacketSend(DataPacketSendEvent event) {
        if(event.getPacket() instanceof ResourcePackStackPacket) {
            ((ResourcePackStackPacket) event.getPacket()).mustAccept = false;
        }
        if (event.getPacket() instanceof ResourcePacksInfoPacket) {
            ((ResourcePacksInfoPacket) event.getPacket()).mustAccept = true;
        }
    }

}
