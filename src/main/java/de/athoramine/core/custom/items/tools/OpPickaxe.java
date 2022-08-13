package de.athoramine.core.custom.items.tools;

import cn.nukkit.item.ItemPickaxeDiamond;
import cn.nukkit.item.customitem.ItemCustomTool;

public class OpPickaxe extends ItemCustomTool {

    public OpPickaxe() {
        super("athora:op_pickaxe", "Geile OP Pickaxe", "athora_op_pickaxe");
    }

    @Override
    public int getMaxDurability() {
        return 1000;
    }

    @Override
    public boolean isPickaxe () {
        return true;
    }

    @Override
    public int getTier() {
        return ItemPickaxeDiamond.TIER_NETHERITE;
    }

    @Override
    public int getAttackDamage() {
        return 8;
    }

}
