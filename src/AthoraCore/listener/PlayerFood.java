package AthoraCore.listener;

import AthoraCore.main.Main;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;

public class PlayerFood implements Listener {

    private Main plugin;

    public PlayerFood(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(PlayerFoodLevelChangeEvent event) {
        event.setFoodLevel(20);
        event.setFoodSaturationLevel(100);
        event.setCancelled(true);
    }

}
