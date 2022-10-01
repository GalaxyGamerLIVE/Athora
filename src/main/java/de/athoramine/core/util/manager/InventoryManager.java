package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.main.Main;
import cn.nukkit.Player;

public class InventoryManager {

//    public static String parseInventoryContentToString(Player player) {
//        String inventory = player.getInventory().getContents().toString();
//        inventory = inventory.replace("{", "");
//        inventory = inventory.replace("}", "");
//        return inventory;
//    }

//    public static void setPlayerInventoryByContentString(Player player, String inventoryContent) {
//        player.getInventory().clearAll();
//        Map<Integer, Item> parsedInventory = new LinkedHashMap<>();
//        for (String keyValue : inventoryContent.split(" *, * ")) {
//            String[] pairs = keyValue.split(" *= *", 2);
////            player.sendMessage(pairs[0] + " " + pairs[1]);
//
////            StringBuffer idString = new StringBuffer(pairs[1]);
////            idString.replace(idString.indexOf(" ("), idString.indexOf(":"), "")
//            int id = Integer.parseInt(pairs[1].substring((pairs[1].indexOf(" (") + 2), pairs[1].indexOf(":")));
//            int meta = Integer.parseInt(pairs[1].substring((pairs[1].indexOf(":") + 1), pairs[1].indexOf(")")));
//            String countString = pairs[1].substring((pairs[1].indexOf(")x") + 2));
//            int countIndex = countString.indexOf(" ");
//            if (countIndex >= 0) countString = countString.substring(0, countIndex);
//            int count = Integer.parseInt(countString);
//            int slot = Integer.parseInt(pairs[0]);
//            String name = pairs[1].substring(0, pairs[1].indexOf(" (")).replace("Item ", "");
////            String tag = "";
////            if (pairs[1].indexOf("tags="))
//
//            player.sendMessage(keyValue);
//            player.sendMessage("Slot: " + slot);
//            player.sendMessage("Name: " + name);
//            player.sendMessage("ID: " + id);
//            player.sendMessage("Meta: " + meta);
//            player.sendMessage("Count: " + count);
//            player.sendMessage("Tag: ");
////            break;
//
//            Item item = new Item(id, meta, count, name);
//            player.getInventory().setItem(slot, item, true);
//            player.getInventory().
////            parsedInventory.put(slot, item);
//        }
////        player.getInventory().setContents(parsedInventory);
//    }

    public static void loadPlayerInventory(Main plugin, Player targetPlayer) {
        if (AthoraPlayer.getInventory(targetPlayer) != null) {
            targetPlayer.getInventory().clearAll();
            targetPlayer.getInventory().setContents(plugin.getItemAPI().invFromString(AthoraPlayer.getInventory(targetPlayer)));
        }
    }

    public static void savePlayerInventory(Player player, Main plugin) {
        if (player.getInventory() != null) {
            String inventory = plugin.getItemAPI().invToString(player.getInventory());
            AthoraPlayer.setInventory(player, inventory);
        }
    }

}
