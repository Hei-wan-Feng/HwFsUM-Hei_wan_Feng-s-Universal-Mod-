package com.heiwanfeng.hwfsum.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, "hwfsum");
    
    public static final Map<DyeColor, RegistryObject<Block>> COLORFUL_LAMPS = new EnumMap<>(DyeColor.class);
    
    static {
        // 注册所有颜色的灯
        for (DyeColor color : DyeColor.values()) {
            COLORFUL_LAMPS.put(color, BLOCKS.register(
                color.getName() + "_colorful_lamp",
                () -> new ColorfulLampBlock(color)
            ));
        }
    }
    
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
    
    public static Block getLampForColor(DyeColor color) {
        return COLORFUL_LAMPS.get(color).get();
    }
}