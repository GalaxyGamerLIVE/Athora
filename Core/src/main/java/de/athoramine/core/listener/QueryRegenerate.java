package de.athoramine.core.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.QueryRegenerateEvent;
import de.athoramine.core.main.Main;

public class QueryRegenerate implements Listener {

    private Main plugin;

    public QueryRegenerate(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQueryRegenerate(QueryRegenerateEvent event) {
        event.setServerName("§l§6Athora §r§7§kii§r");
    }

}
