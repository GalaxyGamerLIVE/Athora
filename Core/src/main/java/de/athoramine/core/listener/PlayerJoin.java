package de.athoramine.core.listener;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.InterestManager;
import de.athoramine.core.util.manager.LobbyItemManager;
import de.athoramine.core.util.manager.PlaytimeManager;
import de.athoramine.core.util.manager.SalaryManager;
import de.athoramine.core.util.manager.ScoreboardManager;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;

public class PlayerJoin implements Listener {

    private final Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase("120c1baf-8e68-3d00-900a-43ebad0c7987") || event.getPlayer().getUniqueId().toString().equalsIgnoreCase("19af4a06-a42d-37dd-acd1-efa507833851")) {
            event.getPlayer().kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, "Blacklist");
            Server.getInstance().getLogger().info(event.getPlayer().getName() + " joined but is on blacklist!");
        } else {
            if (AthoraPlayer.isNewPlayer(event.getPlayer())) {
                AthoraPlayer.createNewPlayer(event.getPlayer());
                event.getPlayer().getInventory().addItem(Item.get(ItemID.IRON_PICKAXE));
                event.getPlayer().getInventory().addItem(Item.get(ItemID.IRON_AXE));
                for (int i = 0; i < 32; i++) {
                    event.getPlayer().getInventory().addItem(Item.get(ItemID.BREAD));
                }
                plugin.getServer().getScheduler().scheduleDelayedTask(() -> {
                    event.getPlayer().sendMessage("§l§7------ §aWillkommen auf Athora! §7------§r\n\n" +
                            "§fDa du neu hier bist, empfehlen wir dir die Anfänger Tipps\n" +
                            "im §9/warp tutorial§f Bereich anzusehen!\n" +
                            "\n" +
                            "§7Indem du auf dem Server spielst stimmst du dem Server §lRegelwerk§r§7 zu.\n" +
                            "§7Das Regelwerk Findest du in unserem Discord:§f https://discord.gg/rdh5EdEjbm\n" +
                            "\n" +
                            "§a§l- Viel Spaß!");
                }, 10);
            }
            PlaytimeManager.trackPlayer(event.getPlayer());
            ScoreboardManager.loadScoreboard(event.getPlayer());

//            InventoryManager.loadPlayerInventory(plugin, event.getPlayer());

//            ExperienceManager.loadExperience(event.getPlayer());

            if (!LobbyItemManager.hasLobbyItem(event.getPlayer())) {
                LobbyItemManager.setLobbyItem(event.getPlayer());
            }

            SalaryManager.trackPlayer(event.getPlayer());

            if (SalaryManager.reachedMinSalaryTime(event.getPlayer())) {
                event.getPlayer().sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du kannst jetzt dein Gehalt mit " + TextFormat.BLUE + "/gehalt" + TextFormat.GREEN + " abholen!");
            }

            plugin.getServer().getScheduler().scheduleDelayedTask(() -> {
                InterestManager.checkInterest(event.getPlayer(), true);
            }, 150);
        }
    }

}
