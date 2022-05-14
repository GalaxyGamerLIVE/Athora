package AthoraCore.listener;

import AthoraCore.main.Main;
import AthoraCore.util.configs.GeneralConfig;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private final Main plugin;

    public PlayerDeath(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!GeneralConfig.generalConfig.getStringList("no_keep_inventory_worlds").contains(event.getEntity().getLevelName())) {
            event.setKeepInventory(true);
            event.setKeepExperience(true);
        }
    }

}
