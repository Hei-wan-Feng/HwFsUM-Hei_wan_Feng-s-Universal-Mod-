package com.heiwanfeng.hwfsum.command;

import com.heiwanfeng.hwfsum.HWFUniversalMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hwfsum")
public class TickCommand {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(Commands.literal("tick")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("rate")
                .then(Commands.argument("rate", FloatArgumentType.floatArg(0.1f, 1000.0f))
                    .executes(context -> setTickRate(context, FloatArgumentType.getFloat(context, "rate")))
                )
            )
            .then(Commands.literal("query")
                .executes(context -> queryTickRate(context))
            )
            .then(Commands.literal("freeze")
                .executes(context -> freezeTicks(context))
            )
            .then(Commands.literal("unfreeze")
                .executes(context -> unfreezeTicks(context))
            )
            .then(Commands.literal("step")
                .then(Commands.argument("ticks", IntegerArgumentType.integer(1, 1000))
                    .executes(context -> stepTicks(context, IntegerArgumentType.getInteger(context, "ticks")))
                )
            )
            .then(Commands.literal("sprint")
                .then(Commands.argument("duration", IntegerArgumentType.integer(1, 3600))
                    .executes(context -> sprintTicks(context, IntegerArgumentType.getInteger(context, "duration")))
                )
            )
        );
    }

    private static int setTickRate(CommandContext<CommandSourceStack> context, float rate) {
        HWFUniversalMod.SERVER_TICK_RATE = rate;
        context.getSource().sendSuccess(() -> Component.literal("已将游戏刻率设置为: " + rate), true);
        return 1;
    }

    private static int queryTickRate(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("当前游戏刻率: " + HWFUniversalMod.SERVER_TICK_RATE), false);
        return 1;
    }

    private static int freezeTicks(CommandContext<CommandSourceStack> context) {
        HWFUniversalMod.IS_TICK_FROZEN = true;
        context.getSource().sendSuccess(() -> Component.literal("游戏时间已冻结"), true);
        return 1;
    }

    private static int unfreezeTicks(CommandContext<CommandSourceStack> context) {
        HWFUniversalMod.IS_TICK_FROZEN = false;
        context.getSource().sendSuccess(() -> Component.literal("游戏时间已恢复"), true);
        return 1;
    }

    private static int stepTicks(CommandContext<CommandSourceStack> context, int ticks) {
        MinecraftServer server = context.getSource().getServer();
        for (int i = 0; i < ticks; i++) {
            if (!HWFUniversalMod.IS_TICK_FROZEN) {
                server.tickServer(() -> true);
            }
        }
        context.getSource().sendSuccess(() -> Component.literal("已步进 " + ticks + " 个游戏刻"), true);
        return 1;
    }

    private static int sprintTicks(CommandContext<CommandSourceStack> context, int duration) {
        float originalRate = HWFUniversalMod.SERVER_TICK_RATE;
        HWFUniversalMod.SERVER_TICK_RATE = 1000.0f; // 最大速度
        
        // 创建一个定时任务来恢复原速率
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    HWFUniversalMod.SERVER_TICK_RATE = originalRate;
                    context.getSource().sendSuccess(() -> Component.literal("冲刺模式结束，刻率已恢复"), true);
                }
            },
            duration * 50 // 转换为毫秒
        );
        
        context.getSource().sendSuccess(() -> Component.literal("已开启冲刺模式，持续 " + duration + " 秒"), true);
        return 1;
    }
}