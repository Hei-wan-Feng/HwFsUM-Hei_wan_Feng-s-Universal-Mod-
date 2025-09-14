package com.heiwanfeng.hwfsum.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = "hwfsum", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.BooleanValue DEFAULT_PVP;
    public static final ForgeConfigSpec.BooleanValue DEFAULT_ENABLE_COMMAND_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue DEFAULT_SPAWN_MONSTERS;
    public static final ForgeConfigSpec.BooleanValue DEFAULT_SHOW_DAYS_PLAYED;
    public static final ForgeConfigSpec.IntValue DEFAULT_MINECART_MAX_SPEED;
    public static final ForgeConfigSpec.BooleanValue DEFAULT_QUICK_BRIDGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_DEATH_CHAT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_DISCONNECTED_CHAT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_DEATH_COMMANDS;
    
    static {
        BUILDER.push("Game Rules Default Settings");
        
        DEFAULT_PVP = BUILDER.comment("Default PVP setting").define("defaultPvP", true);
        DEFAULT_ENABLE_COMMAND_BLOCKS = BUILDER.comment("Default command blocks setting").define("defaultEnableCommandBlocks", true);
        DEFAULT_SPAWN_MONSTERS = BUILDER.comment("Default monster spawning setting").define("defaultSpawnMonsters", true);
        DEFAULT_SHOW_DAYS_PLAYED = BUILDER.comment("Default show days played setting").define("defaultShowDaysPlayed", true);
        DEFAULT_MINECART_MAX_SPEED = BUILDER.comment("Default minecart max speed").defineInRange("defaultMinecartMaxSpeed", 8, 0, 100);
        DEFAULT_QUICK_BRIDGE = BUILDER.comment("Default quick bridge setting").define("defaultQuickBridge", true);
        
        BUILDER.pop();
        
        BUILDER.push("Chat Settings");
        ENABLE_DEATH_CHAT = BUILDER.comment("Enable chat in death screen").define("enableDeathChat", true);
        ENABLE_DISCONNECTED_CHAT = BUILDER.comment("Enable chat in disconnected screen").define("enableDisconnectedChat", true);
        ALLOWED_DEATH_COMMANDS = BUILDER.comment("Commands allowed in death screen")
            .defineList("allowedDeathCommands", Arrays.asList("help", "list", "msg", "tell", "whisper", "w", "me", "say"), 
                it -> it instanceof String);
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
}