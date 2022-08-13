package de.athoramine.core.custom.items.tools;

import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.customitem.ItemCustomTool;

public class TestSword extends ItemCustomTool {

    public TestSword() {
        super("athora:admin_sword", "Schwert für Admins", "athora_custom_sword_admin");
    }

    @Override
    public int getMaxDurability() {
        return 1000;
    }

    @Override
    public int getTier() {
        return ItemSwordDiamond.TIER_DIAMOND;
    }

    @Override
    public int getAttackDamage() {
        return 30;
    }

}
