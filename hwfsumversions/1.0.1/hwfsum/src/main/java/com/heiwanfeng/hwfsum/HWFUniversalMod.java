package com.heiwanfeng.hwfsum;

import com.heiwanfeng.hwfsum.block.ModBlocks;
import com.heiwanfeng.hwfsum.config.ModConfig;
import com.heiwanfeng.hwfsum.event.GameRuleHandlers;
import com.heiwanfeng.hwfsum.event.QuickBridgeHandler;
import com.heiwanfeng.hwfsum.item.ModItems;
import com.heiwanfeng.hwfsum.recipe.ModRecipes;
import com.heiwanfeng.hwfsum.screen.ModConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("hwfsum")
public class HWFUniversalMod {
    public static final String MOD_ID = "hwfsum";
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static float SERVER_TICK_RATE = 20.0f;
    public static boolean IS_TICK_FROZEN = false;
    public static boolean QUICK_BRIDGE_ENABLED = true;

    public HWFUniversalMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(GameRuleHandlers.class);
        MinecraftForge.EVENT_BUS.register(QuickBridgeHandler.class);
        
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC, "hwfsum-common.toml");
        
        // 注册方块、物品和配方
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Hei_wan_Feng's Universal Mod 初始化完成！");
        
        event.enqueueWork(() -> {
            com.heiwanfeng.hwfsum.network.ChatPacketHandler.register();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (mc, parent) -> new ModConfigScreen(parent)
                )
            );
        }
    }
}