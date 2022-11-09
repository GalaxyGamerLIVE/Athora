package de.athoramine.core.util;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ItemAPI {

    public String invToString(Inventory inventory) {
        StringBuilder stringInv = new StringBuilder();
        inventory.getContents().forEach(((integer, item) -> stringInv.append(itemToString(integer, item)).append(";")));
        if (stringInv.toString().length() == 0) return "";
        return stringInv.substring(0, stringInv.toString().length() - 1);
    }

    public Map<Integer, Item> invFromString(String string) {
        if (!string.equalsIgnoreCase("empty")) {
            String[] itemStrings = string.split(";");
            final Map<Integer, Item> loadedInv = new HashMap<>();
            for (String str : itemStrings) {
                ItemWithSlot its = itemFromString(str);
                loadedInv.put(its.getSlot(), its.getItem());
            }
            return loadedInv;
        } else return new HashMap<>();
    }

    // itemInfo
    // slot:id:damage:count:CompoundTag(base64)

    public String itemToString(int slot, Item item) {
        if (item.getId() == 255) { // custom items
            return slot + ":" + item.getId() + ":" + item.getNamespaceId().replace(":", "=") + ":" + item.getDamage() + ":" + item.getCount() + ":" + (item.hasCompoundTag() ? bytesToBase64(item.getCompoundTag()) : "not");
        } else {
            return slot + ":" + item.getId() + ":" + item.getDamage() + ":" + item.getCount() + ":" + (item.hasCompoundTag() ? bytesToBase64(item.getCompoundTag()) : "not");
        }
    }

    private String bytesToBase64(byte[] src) {
        if (src == null || src.length <= 0) return "not";
        return Base64.getEncoder().encodeToString(src);
    }

    // itemInfo
    // slot:id:damage:count:CompoundTag(base64)

    public ItemWithSlot itemFromString(String itemString) throws NumberFormatException {
        String[] info = itemString.split(":");
        int slot = Integer.parseInt(info[0]);
        int id = Integer.parseInt(info[1]);
        Item item;
        if (id == 255) { // custom items
            item = Item.fromString(info[2].replace("=", ":"));
            item.setDamage(Integer.parseInt(info[3]));
            item.setCount(Integer.parseInt(info[4]));
            if (!info[5].equals("not")) item.setCompoundTag(base64ToBytes(info[5]));
        } else {
            item = Item.get(
                    Integer.parseInt(info[1]),
                    Integer.parseInt(info[2]),
                    Integer.parseInt(info[3])
            );
            if (!info[4].equals("not")) item.setCompoundTag(base64ToBytes(info[4]));
        }
        return new ItemWithSlot(slot, item);
    }

    private byte[] base64ToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) return null;
        return Base64.getDecoder().decode(hexString);
    }


}
