package AthoraCore.listener;

import AthoraCore.main.Main;
import AthoraCore.util.manager.InventoryManager;
import AthoraCore.util.manager.PlaytimeManager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final Main plugin;

    public PlayerQuit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Main.scoreboards.remove(event.getPlayer());

        PlaytimeManager.untrackPlayer(event.getPlayer());

        InventoryManager.savePlayerInventory(event.getPlayer(), plugin);
    }

}
