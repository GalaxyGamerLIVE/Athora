package de.athoramine.core.listener;

import de.athoramine.core.main.Main;
import de.athoramine.core.util.manager.InventoryManager;
import de.athoramine.core.util.manager.LobbyItemManager;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
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
        if (LobbyItemManager.isLobbyItem(event.getSourceItem())) {
            event.setCancelled(true);
        } else {
            for (Player player : event.getInventory().getViewers()) {
                InventoryManager.savePlayerInventory(player, plugin);
            }
        }
    }

    @EventHandler
    public void onChange(InventoryMoveItemEvent event) {
        if (LobbyItemManager.isLobbyItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChange(PlayerDropItemEvent event) {
        if (LobbyItemManager.isLobbyItem(event.getItem())) {
            event.setCancelled(true);
        } else {
            InventoryManager.savePlayerInventory(event.getPlayer(), plugin);
        }
    }
}

