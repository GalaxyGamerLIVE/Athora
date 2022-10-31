package de.athoramine.core.custom.items.tools;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.item.ItemPickaxeDiamond;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

public class AdminPickaxe extends ItemCustomTool {

    public AdminPickaxe() {
        super("athora:op_pickaxe", "Admin Spitzhacke", "athora_op_pickaxe");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .speed(10)
                .addExtraBlockTag(BlockState.of("minecraft:nether_brick").getBlock(), 10)
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
    public boolean isPickaxe () {
        return true;
    }

    @Override
    public int getAttackDamage() {
        return 8;
    }

    @Override
    public int getEnchantAbility() {
        return 50;
    }

    @Override
    public int getTier() {
        return ItemCustomTool.TIER_NETHERITE;
    }

}
