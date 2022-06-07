package AthoraCore.listener;

import AthoraCore.main.Main;
import AthoraCore.util.manager.ExperienceManager;
import cn.nukkit.entity.Attribute;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.UpdateAttributesPacket;

public class ListenDataPacket implements Listener {

    private Main plugin;

    public ListenDataPacket(Main main) {
        plugin = main;
    }

    @EventHandler
    public void onDataPacketSend(DataPacketSendEvent event) {
        if (event.getPacket() instanceof UpdateAttributesPacket) {
            for (Attribute attribute : ((UpdateAttributesPacket) event.getPacket()).entries) {
                if (attribute.getId() == Attribute.EXPERIENCE || attribute.getId() == Attribute.EXPERIENCE_LEVEL) {
                    if (ExperienceManager.isPlayerLoaded(event.getPlayer())) {
                        ExperienceManager.saveExperience(event.getPlayer());
                    }
                }
            }
        }
    }

}
