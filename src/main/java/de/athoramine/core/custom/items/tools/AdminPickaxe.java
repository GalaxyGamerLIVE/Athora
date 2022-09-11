package de.athoramine.core.custom.items.tools;

import cn.nukkit.item.ItemPickaxeDiamond;
import cn.nukkit.item.customitem.ItemCustomTool;

public class AdminPickaxe extends ItemCustomTool {

    public AdminPickaxe() {
        super("athora:op_pickaxe", "Admin Spitzhacke", "athora_op_pickaxe");
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

    @Override
    public int getDestroySpeeds() {
        return 1;
    }

}
