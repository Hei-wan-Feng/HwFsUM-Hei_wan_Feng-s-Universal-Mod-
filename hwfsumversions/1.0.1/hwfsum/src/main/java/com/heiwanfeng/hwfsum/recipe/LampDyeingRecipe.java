package com.heiwanfeng.hwfsum.recipe;

import com.heiwanfeng.hwfsum.block.ModBlocks;
import com.heiwanfeng.hwfsum.item.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import com.heiwanfeng.hwfsum.item.DyeableLampItem;

public class LampDyeingRecipe extends CustomRecipe {
    public static final SimpleCraftingRecipeSerializer<LampDyeingRecipe> SERIALIZER = 
        new SimpleCraftingRecipeSerializer<>(LampDyeingRecipe::new);
    
    public LampDyeingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }
    
    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack lampStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;
        int itemCount = 0;
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (stack.getItem() instanceof DyeableLampItem) {
                    if (!lampStack.isEmpty()) return false; // 只能有一个灯
                    lampStack = stack;
                } else if (stack.getItem() instanceof DyeItem) {
                    if (!dyeStack.isEmpty()) return false; // 只能有一个染料
                    dyeStack = stack;
                } else {
                    return false; // 只能有灯和染料
                }
            }
        }
        
        return itemCount == 2 && !lampStack.isEmpty() && !dyeStack.isEmpty();
    }
    
    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack lampStack = ItemStack.EMPTY;
        DyeColor dyeColor = null;
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof DyeableLampItem lampItem) {
                    lampStack = stack;
                } else if (stack.getItem() instanceof DyeItem dyeItem) {
                    dyeColor = dyeItem.getDyeColor();
                }
            }
        }
        
        if (lampStack.isEmpty() || dyeColor == null) {
            return ItemStack.EMPTY;
        }
        
        // 创建新的染色灯
        return new ItemStack(ModItems.COLORFUL_LAMP_ITEMS.get(dyeColor).get());
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}