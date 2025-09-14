package com.heiwanfeng.hwfsum.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = "hwfsum")
public class RandomCommand {
    
    private static final Random RANDOM = new Random();
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(Commands.literal("random")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("min", IntegerArgumentType.integer())
                .then(Commands.argument("max", IntegerArgumentType.integer())
                    .executes(context -> generateRandom(
                        context, 
                        IntegerArgumentType.getInteger(context, "min"),
                        IntegerArgumentType.getInteger(context, "max"),
                        1
                    ))
                    .then(Commands.argument("count", IntegerArgumentType.integer(1, 100))
                        .executes(context -> generateRandom(
                            context,
                            IntegerArgumentType.getInteger(context, "min"),
                            IntegerArgumentType.getInteger(context, "max"),
                            IntegerArgumentType.getInteger(context, "count")
                        ))
                    )
                )
            )
        );
    }

    private static int generateRandom(CommandContext<CommandSourceStack> context, int min, int max, int count) {
        if (min > max) {
            context.getSource().sendFailure(Component.literal("最小值不能大于最大值"));
            return 0;
        }
        
        if (count == 1) {
            int result = ThreadLocalRandom.current().nextInt(min, max + 1);
            context.getSource().sendSuccess(() -> 
                Component.translatable("hwfsum.command.random.success", min, max, result), 
                false
            );
            return 1;
        } else {
            List<Integer> results = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                results.add(ThreadLocalRandom.current().nextInt(min, max + 1));
            }
            
            String resultString = String.join(", ", results.stream().map(String::valueOf).toArray(String[]::new));
            context.getSource().sendSuccess(() -> 
                Component.translatable("hwfsum.command.random.multiple", min, max, resultString), 
                false
            );
            return count;
        }
    }
}