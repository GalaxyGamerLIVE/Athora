package de.athoramine.core.util;

import cn.nukkit.item.Item;

public class ItemWithSlot {

    private final Integer slot;
    private final Item item;

    public ItemWithSlot(Integer slot, Item item) {
        this.slot = slot;
        this.item = item;
    }

    public Integer getSlot() {
        return slot;
    }

    public Item getItem() {
        return item;
    }
}
