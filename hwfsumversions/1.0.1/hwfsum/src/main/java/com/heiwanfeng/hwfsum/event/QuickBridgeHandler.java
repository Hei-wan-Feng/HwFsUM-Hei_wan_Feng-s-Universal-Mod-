package com.heiwanfeng.hwfsum.event;

import com.heiwanfeng.hwfsum.HWFUniversalMod;
import com.heiwanfeng.hwfsum.util.BlockSelectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hwfsum")
public class QuickBridgeHandler {
    
    public static BlockPos lastPreviewPos = null;
    public static BlockState lastPreviewState = null;
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!HWFUniversalMod.QUICK_BRIDGE_ENABLED) return;
        
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        
        // 检查手中是否有方块
        if (!(heldItem.getItem() instanceof BlockItem)) return;
        
        // 获取玩家视线目标
        HitResult hitResult = player.pick(5.0, 0.0F, false);
        
        // 如果玩家对着空气（没有命中方块）
        if (hitResult.getType() == HitResult.Type.MISS) {
            // 获取玩家脚下的方块
            BlockPos footPos = player.blockPosition().below();
            BlockState footState = player.level().getBlockState(footPos);
            
            // 如果脚下有方块，则使用脚下方块的类型
            if (!footState.isAir()) {
                BlockItem footBlockItem = BlockSelectionHelper.getBlockItemFromState(footState);
                if (footBlockItem != null) {
                    // 替换玩家手中的物品为脚下方块类型
                    if (!player.isCreative()) {
                        ItemStack newStack = new ItemStack(footBlockItem, heldItem.getCount());
                        player.getInventory().setItem(player.getInventory().selected, newStack);
                    }
                    
                    // 设置预览信息
                    lastPreviewPos = calculatePlacementPos(player);
                    lastPreviewState = footState;
                    
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        // 清除预览信息
        lastPreviewPos = null;
        lastPreviewState = null;
    }
    
    private static BlockPos calculatePlacementPos(Player player) {
        // 计算玩家面前的位置
        double reachDistance = 4.5;
        double yaw = Math.toRadians(player.getYRot());
        double pitch = Math.toRadians(player.getXRot());
        
        double x = -Math.sin(yaw) * Math.cos(pitch) * reachDistance;
        double y = -Math.sin(pitch) * reachDistance;
        double z = Math.cos(yaw) * Math.cos(pitch) * reachDistance;
        
        BlockPos playerPos = player.blockPosition();
        return new BlockPos(
            (int) Math.round(playerPos.getX() + x),
            (int) Math.round(playerPos.getY() + player.getEyeHeight() + y),
            (int) Math.round(playerPos.getZ() + z)
        );
    }
    
    public static boolean shouldShowPreview() {
        return HWFUniversalMod.QUICK_BRIDGE_ENABLED && lastPreviewPos != null && lastPreviewState != null;
    }
}