package AthoraCore.listener;

import AthoraCore.main.Main;
import AthoraCore.util.manager.InventoryManager;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;

public class InventoryChange implements Listener {

    private Main plugin;

    public InventoryChange(Main main) {
        this.plugin = main;
    }

    @EventHandler
    public void onChange(InventoryPickupItemEvent event) {
        for (Player player : event.getInventory().getViewers()) {
            InventoryManager.savePlayerInventory(player, plugin);
        }
    }

    @EventHandler
    public void onChange(InventoryClickEvent event) {
        for (Player player : event.getInventory().getViewers()) {
            InventoryManager.savePlayerInventory(player, plugin);
        }
    }

    @EventHandler
    public void onChange(PlayerDropItemEvent event) {
        InventoryManager.savePlayerInventory(event.getPlayer(), plugin);
    }
}

