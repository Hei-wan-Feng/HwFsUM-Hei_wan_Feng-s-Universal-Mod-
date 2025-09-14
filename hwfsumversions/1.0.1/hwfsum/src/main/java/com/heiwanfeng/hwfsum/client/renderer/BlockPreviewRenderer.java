package com.heiwanfeng.hwfsum.client.renderer;

import com.heiwanfeng.hwfsum.event.QuickBridgeHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hwfsum", value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class BlockPreviewRenderer {
    
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (!QuickBridgeHandler.shouldShowPreview()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        BlockPos previewPos = QuickBridgeHandler.lastPreviewPos;
        if (previewPos == null) return;
        
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
        
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        double x = previewPos.getX() - cameraPos.x;
        double y = previewPos.getY() - cameraPos.y;
        double z = previewPos.getZ() - cameraPos.z;
        
        // 使用 AABB 绘制方块轮廓
        AABB aabb = new AABB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        LevelRenderer.renderLineBox(
            poseStack,
            vertexConsumer,
            aabb,
            1.0F, 0.0F, 0.0F, 1.0F // 红色轮廓
        );
        
        // 绘制半透明预览方块
        if (QuickBridgeHandler.lastPreviewState != null) {
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            
            mc.getBlockRenderer().renderSingleBlock(
                QuickBridgeHandler.lastPreviewState,
                poseStack,
                bufferSource,
                0xF000F0,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                RenderType.translucent()
            );
            
            poseStack.popPose();
        }
    }
}