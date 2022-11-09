package de.athoramine.core.listener;

import de.athoramine.core.main.Main;
import de.athoramine.core.util.manager.BossBarManager;
import de.athoramine.core.util.manager.ExperienceManager;
import de.athoramine.core.util.manager.InventoryManager;
import de.athoramine.core.util.manager.PlaytimeManager;
import de.athoramine.core.util.manager.SalaryManager;
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
        InventoryManager.savePlayerInventory(event.getPlayer(), this.plugin);
        SalaryManager.untrackPlayer(event.getPlayer());
        if (BossBarManager.playerHasBossBar(event.getPlayer())) {
            BossBarManager.removeBossBar(event.getPlayer());
        }
        ExperienceManager.saveExperience(event.getPlayer());
    }
}
