package de.athoramine.core.custom.items.tools;

import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

public class AdminSword extends ItemCustomTool {

    public AdminSword() {
        super("athora:admin_sword", "Admin Schwert", "athora_custom_sword_admin");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .allowOffHand(true)
                .handEquipped(true)
                .foil(true)
                .build();
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

    @Override
    public int getEnchantAbility() {
        return 20;
    }

    @Override
    public boolean isSword() {
        return true;
    }

}
