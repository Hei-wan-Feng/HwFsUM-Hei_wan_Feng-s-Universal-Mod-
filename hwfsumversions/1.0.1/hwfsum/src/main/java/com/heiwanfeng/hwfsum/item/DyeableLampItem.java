package com.heiwanfeng.hwfsum.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class DyeableLampItem extends BlockItem {
    private final DyeColor color;
    
    public DyeableLampItem(Block block, Item.Properties properties, DyeColor color) {
        super(block, properties);
        this.color = color;
    }
    
    public DyeColor getColor() {
        return color;
    }
    
    @Override
    public String getDescriptionId(ItemStack stack) {
        return "block.hwfsum.colorful_lamp";
    }
}