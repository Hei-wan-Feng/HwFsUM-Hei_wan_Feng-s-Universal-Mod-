package com.heiwanfeng.hwfsum.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = 
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, "hwfsum");
    
    public static final RegistryObject<RecipeSerializer<?>> LAMP_DYEING = 
        SERIALIZERS.register("lamp_dyeing", () -> LampDyeingRecipe.SERIALIZER);
    
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}