package de.athoramine.core.listener;

import de.athoramine.core.main.Main;
import de.athoramine.core.util.manager.ExperienceManager;
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

    // NOT REGISTERED! Only here for an example the listen packages process. Maybe needed in future
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
