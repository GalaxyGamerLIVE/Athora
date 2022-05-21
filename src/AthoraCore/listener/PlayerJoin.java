package AthoraCore.listener;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.main.Main;
import AthoraCore.util.manager.InterestManager;
import AthoraCore.util.manager.InventoryManager;
import AthoraCore.util.manager.PlaytimeManager;
import AthoraCore.util.manager.ScoreboardManager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (AthoraPlayer.isNewPlayer(event.getPlayer())) {
            AthoraPlayer.createNewPlayer(event.getPlayer());
        }
        PlaytimeManager.trackPlayer(event.getPlayer());
        ScoreboardManager.loadScoreboard(event.getPlayer());

        InventoryManager.loadPlayerInventory(plugin, event.getPlayer());

        InterestManager.checkInterest(event.getPlayer(), true);
    }

}
