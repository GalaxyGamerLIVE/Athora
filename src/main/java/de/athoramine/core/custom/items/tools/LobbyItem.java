package de.athoramine.core.custom.items.tools;

import cn.nukkit.item.customitem.ItemCustomTool;

public class LobbyItem extends ItemCustomTool {

    public LobbyItem() {
        super("athora:lobby_item", "§l§6Menü", "athora_lobby_item");
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

    @Override
    public int getEnchantableValue() {
        return 0;
    }
}
