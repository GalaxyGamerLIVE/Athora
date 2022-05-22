package AthoraCore.listener;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.main.Main;
import AthoraCore.util.manager.InterestManager;
import AthoraCore.util.manager.InventoryManager;
import AthoraCore.util.manager.MineManager;
import AthoraCore.util.manager.PlaytimeManager;
import AthoraCore.util.manager.ScoreboardManager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;

public class PlayerJoin implements Listener {

    private final Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (AthoraPlayer.isNewPlayer(event.getPlayer())) {
            AthoraPlayer.createNewPlayer(event.getPlayer());
            event.getPlayer().getInventory().addItem(Item.get(ItemID.IRON_PICKAXE));
            event.getPlayer().getInventory().addItem(Item.get(ItemID.IRON_AXE));
            for (int i = 0; i < 32; i++) {
                event.getPlayer().getInventory().addItem(Item.get(ItemID.BREAD));
            }
            plugin.getServer().getScheduler().scheduleDelayedTask(() -> {
                event.getPlayer().sendMessage("§6§l Willkommen auf Athora!\n§aDa du neu hier bist, empfehlen wir dir die Anfänger Tipps\nim §9/warp tutorial§a Bereich anzusehen, viel spaß!");
            }, 10);
        }
        PlaytimeManager.trackPlayer(event.getPlayer());
        ScoreboardManager.loadScoreboard(event.getPlayer());

        InventoryManager.loadPlayerInventory(plugin, event.getPlayer());

        InterestManager.checkInterest(event.getPlayer(), true);
    }

}
