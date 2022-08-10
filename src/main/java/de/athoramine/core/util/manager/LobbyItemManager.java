package de.athoramine.core.util.manager;

import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LobbyItemManager {

    public static final Map<UUID, Player> openedForms = new HashMap<>();

    public static int getLobbyItemSlot(Player player) {
        return player.getInventory().getHotbarSize() - 1;
    }

    public static void setLobbyItem(Player player) {
        int lastHotbarSlot = getLobbyItemSlot(player);
        Item lobbyItem = new Item(345, 0, 1, "§g§kii§r §6Lobby Item §g§kii");
        lobbyItem.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL));
        lobbyItem.setCustomName("§o§g§kii§r §6Lobby Item §o§g§kii");
        lobbyItem.setLore("\nÖffnet das Lobby Fenster!");

        if (player.getInventory().getItem(lastHotbarSlot).getId() != 0) {
            Item usedItem = player.getInventory().getItem(lastHotbarSlot);
            player.getInventory().clear(lastHotbarSlot);
            if (!player.getInventory().canAddItem(usedItem)) {
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "§lDir konnte nicht das Lobby Item gegeben werden, da dein Inventar voll ist! Bitte leere ein Slot in deinem Inventar und betrete den Server neu oder benutze den /lobbyitem on/off Befehl, um das Lobby Item zu bekommen!");
                player.getInventory().setItem(lastHotbarSlot, usedItem);
                return;
            }
            player.getInventory().addItem(usedItem);
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Dir wurde erfolgreich das Lobby Item gesetzt! Dabei wurde ein Item in deinem Inventar auf einem freien Slot verschoben!");
        }
        player.getInventory().setItem(lastHotbarSlot, lobbyItem);
    }

    public static boolean hasLobbyItem(Player player) {
        int lastHotbarSlot = getLobbyItemSlot(player);
        Item item = player.getInventory().getItem(lastHotbarSlot);
        return isLobbyItem(item);
    }

    public static boolean isLobbyItem(Item item) {
        if (item.getId() == 345 && item.getCustomName().equals("§o§g§kii§r §6Lobby Item §o§g§kii") && item.hasEnchantment(Enchantment.ID_PROTECTION_ALL)) {
            return true;
        }
        return false;
    }

}
