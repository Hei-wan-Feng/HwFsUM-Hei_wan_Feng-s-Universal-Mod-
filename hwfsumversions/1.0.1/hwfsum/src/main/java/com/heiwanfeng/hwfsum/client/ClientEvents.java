package com.heiwanfeng.hwfsum.client;

import com.heiwanfeng.hwfsum.event.QuickBridgeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hwfsum", value = Dist.CLIENT)
public class ClientEvents {
    
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        if (event.getKey() == 66) {
            com.heiwanfeng.hwfsum.HWFUniversalMod.QUICK_BRIDGE_ENABLED = 
                !com.heiwanfeng.hwfsum.HWFUniversalMod.QUICK_BRIDGE_ENABLED;
            
            mc.player.displayClientMessage(
                Component.literal(
                    com.heiwanfeng.hwfsum.HWFUniversalMod.QUICK_BRIDGE_ENABLED ? 
                    "快捷搭路模式已启用" : "快捷搭路模式已禁用"
                ),
                true
            );
            
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) return;
        if (!QuickBridgeHandler.shouldShowPreview()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        
        guiGraphics.drawString(
            mc.font,
            "快捷搭路: " + (com.heiwanfeng.hwfsum.HWFUniversalMod.QUICK_BRIDGE_ENABLED ? "启用" : "禁用"),
            screenWidth / 2 - 40,
            screenHeight / 2 + 20,
            0xFFFFFF
        );
        
        if (QuickBridgeHandler.lastPreviewState != null) {
            guiGraphics.drawString(
                mc.font,
                "将放置: " + QuickBridgeHandler.lastPreviewState.getBlock().getName().getString(),
                screenWidth / 2 - 60,
                screenHeight / 2 + 40,
                0x00FF00
            );
        }
    }
}