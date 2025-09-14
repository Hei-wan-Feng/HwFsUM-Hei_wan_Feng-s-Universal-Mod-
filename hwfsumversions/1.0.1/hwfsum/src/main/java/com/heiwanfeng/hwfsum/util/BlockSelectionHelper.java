package com.heiwanfeng.hwfsum.util;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class BlockSelectionHelper {
    
    private static final Map<Block, BlockItem> BLOCK_TO_ITEM_CACHE = new HashMap<>();
    
    static {
        // 初始化缓存
        ForgeRegistries.ITEMS.getValues().forEach(item -> {
            if (item instanceof BlockItem blockItem) {
                BLOCK_TO_ITEM_CACHE.put(blockItem.getBlock(), blockItem);
            }
        });
    }
    
    public static BlockItem getBlockItemFromState(BlockState state) {
        Block block = state.getBlock();
        return BLOCK_TO_ITEM_CACHE.get(block);
    }
    
    public static BlockItem getBlockItemFromBlock(Block block) {
        return BLOCK_TO_ITEM_CACHE.get(block);
    }
    
    // 获取常见的建筑方块
    public static BlockItem getCommonBuildingBlock() {
        return getBlockItemFromBlock(net.minecraft.world.level.block.Blocks.STONE);
    }
}