package com.heiwanfeng.hwfsum.event;

import com.heiwanfeng.hwfsum.gamerule.ModGameRules;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hwfsum")
public class GameRuleHandlers {
    
    @SubscribeEvent
    public static void onMobSpawning(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        
        if (event.getEntity() instanceof Monster) {
            GameRules rules = event.getLevel().getGameRules();
            if (!rules.getBoolean(ModGameRules.RULE_SPAWN_MONSTERS)) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        
        if (event.getTarget() instanceof Player) {
            GameRules rules = event.getEntity().level().getGameRules();
            if (!rules.getBoolean(ModGameRules.RULE_PVP)) {
                event.setCanceled(true);
                event.getEntity().displayClientMessage(net.minecraft.network.chat.Component.literal("PVP已被禁用"), true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onMinecartUpdate(net.minecraftforge.event.entity.EntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof AbstractMinecart minecart)) return;
        
        GameRules rules = minecart.level().getGameRules();
        int maxSpeed = rules.getInt(ModGameRules.RULE_MINECART_MAX_SPEED);
        
        if (minecart.getDeltaMovement().length() > maxSpeed / 8.0) {
            net.minecraft.world.phys.Vec3 motion = minecart.getDeltaMovement().normalize().scale(maxSpeed / 8.0);
            minecart.setDeltaMovement(motion);
        }
    }
    
    @SubscribeEvent
    public static void onCommandBlockActivate(BlockEvent event) {
         // 将 LevelAccessor 转换为 Level
		if (event.getLevel() instanceof net.minecraft.world.level.Level level) {
			GameRules rules = level.getGameRules();
			if (!rules.getBoolean(ModGameRules.RULE_ENABLE_COMMAND_BLOCKS)) {
				event.setCanceled(true);
			}
		}
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        GameRules rules = event.getEntity().level().getGameRules();
        if (rules.getBoolean(ModGameRules.RULE_SHOW_DAYS_PLAYED)) {
            long daysPlayed = event.getEntity().level().getDayTime() / 24000L;
            event.getEntity().displayClientMessage(net.minecraft.network.chat.Component.literal("已游玩天数: " + daysPlayed), false);
        }
    }
}