package com.heiwanfeng.hwfsum.item;

import com.heiwanfeng.hwfsum.block.ModBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, "hwfsum");
    
    public static final Map<DyeColor, RegistryObject<Item>> COLORFUL_LAMP_ITEMS = new EnumMap<>(DyeColor.class);
    
    static {
        // 注册所有颜色的灯物品
        for (DyeColor color : DyeColor.values()) {
            COLORFUL_LAMP_ITEMS.put(color, ITEMS.register(
                color.getName() + "_colorful_lamp",
                () -> new DyeableLampItem(
                    ModBlocks.COLORFUL_LAMPS.get(color).get(),
                    new Item.Properties(),
                    color
                )
            ));
        }
    }
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}