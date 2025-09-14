package com.heiwanfeng.hwfsum.command;

import com.heiwanfeng.hwfsum.util.OfflinePlayerManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hwfsum")
public class OfflinePlayerCommand {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(Commands.literal("offlineplayer")
            .requires(source -> source.hasPermission(4)) // 需要高级权限
            .then(Commands.argument("player", StringArgumentType.string())
                .then(Commands.argument("operation", StringArgumentType.string())
                    .then(Commands.argument("value", StringArgumentType.string())
                        .executes(context -> modifyOfflinePlayer(
                            context,
                            StringArgumentType.getString(context, "player"),
                            StringArgumentType.getString(context, "operation"),
                            StringArgumentType.getString(context, "value")
                        ))
                    )
                )
            )
        );
    }

    private static int modifyOfflinePlayer(CommandContext<CommandSourceStack> context, String playerName, String operation, String value) {
        try {
            boolean success = OfflinePlayerManager.modifyOfflinePlayer(context.getSource().getServer(), playerName, operation, value);
            
            if (success) {
                context.getSource().sendSuccess(() -> 
                    Component.translatable("hwfsum.command.offlineplayer.success", playerName), 
                    true
                );
                return 1;
            } else {
                context.getSource().sendFailure(
                    Component.translatable("hwfsum.command.offlineplayer.not_found", playerName)
                );
                return 0;
            }
        } catch (Exception e) {
            context.getSource().sendFailure(
                Component.translatable("hwfsum.command.offlineplayer.invalid_operation", e.getMessage())
            );
            return 0;
        }
    }
}