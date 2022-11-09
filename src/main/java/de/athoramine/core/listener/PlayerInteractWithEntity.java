package de.athoramine.core.listener;

import cn.nukkit.Server;
import cn.nukkit.dialog.window.FormWindowDialog;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.utils.TextFormat;

public class PlayerInteractWithEntity implements Listener {

    // TODO register event
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {

        Server.getInstance().getLogger().info(TextFormat.clean(event.getEntity().getName()));
        Server.getInstance().getLogger().info(TextFormat.clean(event.getEntity().getNameTag()));


//        FormWindowDialog dialog = new FormWindowDialog("TestTitel", "TestContent", event.getEntity());
//        dialog.addButton("TestButton");
//        if (event.getEntity() instanceof EntityHuman human) {
//            dialog.setSkinData("{\"picker_offsets\":{\"scale\":[1.75,1.75,1.75],\"translate\":[0,0,0]},\"portrait_offsets\":{\"scale\":[1.75,1.75,1.75],\"translate\":[0,-50,0]}}");
//        }
//        dialog.addHandler((p, r) -> p.sendMessage("response"));
//        event.getPlayer().showDialogWindow(dialog);
    }

}
