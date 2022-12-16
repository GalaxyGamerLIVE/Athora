package de.athoramine.core.custom.items.tools;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

public class LobbyItem extends ItemCustomTool {

    public LobbyItem() {
        super("athora:lobby_item", "§l§6Menü", "athora_lobby_item");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.ITEMS)
                .allowOffHand(false)
                .handEquipped(true)
                .foil(true)
                .build();
    }

    @Override
    public boolean isUnbreakable() {
        return true;
    }

    @Override
    public boolean noDamageOnBreak() {
        return true;
    }

    @Override
    public boolean noDamageOnAttack() {
        return true;
    }

}
