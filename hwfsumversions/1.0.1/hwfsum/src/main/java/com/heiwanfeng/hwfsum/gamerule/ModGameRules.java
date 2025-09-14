package com.heiwanfeng.hwfsum.gamerule;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("hwfsum")
public class ModGameRules {

	// 声明所有规则键为 public static
    public static GameRules.Key<GameRules.BooleanValue> RULE_SPAWN_MONSTERS;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_DAYS_PLAYED;
    public static GameRules.Key<GameRules.BooleanValue> RULE_PVP;
    public static GameRules.Key<GameRules.BooleanValue> RULE_ENABLE_COMMAND_BLOCKS;
    public static GameRules.Key<GameRules.BooleanValue> RULE_DO_WEATHER_CYCLE;
    public static GameRules.Key<GameRules.BooleanValue> RULE_DO_WARDEN_SPAWNING;
    public static GameRules.Key<GameRules.BooleanValue> RULE_DROWNING_DAMAGE;
    public static GameRules.Key<GameRules.BooleanValue> RULE_ENDER_PEARLS_VANISH_ON_DEATH;
    public static GameRules.Key<GameRules.BooleanValue> RULE_FALL_DAMAGE;
    public static GameRules.Key<GameRules.BooleanValue> RULE_PROJECTILES_CAN_BREAK_BLOCKS;
    public static GameRules.Key<GameRules.BooleanValue> RULE_MOB_EXPLOSION_DROP_DECAY;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_BORDER_EFFECT;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_COORDINATES;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_DEATH_MESSAGES;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_RECIPE_MESSAGES;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_TAGS;
    public static GameRules.Key<GameRules.BooleanValue> RULE_SHOW_ACCUMULATION_HEIGHT;

    public static GameRules.Key<GameRules.IntegerValue> RULE_MINECART_MAX_SPEED;
    public static GameRules.Key<GameRules.IntegerValue> RULE_RANDOM_TICK_SPEED;
    public static GameRules.Key<GameRules.IntegerValue> RULE_MAX_ENTITY_CRAMMING;
    public static GameRules.Key<GameRules.IntegerValue> RULE_SPAWN_CHUNK_RADIUS;
    public static GameRules.Key<GameRules.IntegerValue> RULE_SPAWN_RADIUS;
    // ... 字段声明保持不变 ...

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            RULE_SPAWN_MONSTERS = registerBooleanRule("spawnMonsters", GameRules.Category.SPAWNING, true);
            RULE_SHOW_DAYS_PLAYED = registerBooleanRule("showDaysPlayed", GameRules.Category.PLAYER, true);
            RULE_PVP = registerBooleanRule("pvp", GameRules.Category.PLAYER, true);
            RULE_ENABLE_COMMAND_BLOCKS = registerBooleanRule("enableCommandBlocks", GameRules.Category.MISC, true);
            RULE_DO_WEATHER_CYCLE = registerBooleanRule("doWeatherCycle", GameRules.Category.UPDATES, true);
            RULE_DO_WARDEN_SPAWNING = registerBooleanRule("doWardenSpawning", GameRules.Category.SPAWNING, true);
            RULE_DROWNING_DAMAGE = registerBooleanRule("drowningDamage", GameRules.Category.MISC, true);
            RULE_ENDER_PEARLS_VANISH_ON_DEATH = registerBooleanRule("enderPearlsVanishOnDeath", GameRules.Category.DROPS, true);
            RULE_FALL_DAMAGE = registerBooleanRule("fallDamage", GameRules.Category.MISC, true);
            RULE_PROJECTILES_CAN_BREAK_BLOCKS = registerBooleanRule("projectilesCanBreakBlocks", GameRules.Category.MISC, false);
            RULE_MOB_EXPLOSION_DROP_DECAY = registerBooleanRule("mobExplosionDropDecay", GameRules.Category.DROPS, true);
            RULE_SHOW_BORDER_EFFECT = registerBooleanRule("showBorderEffect", GameRules.Category.MISC, true);
            RULE_SHOW_COORDINATES = registerBooleanRule("showCoordinates", GameRules.Category.PLAYER, true);
            RULE_SHOW_DEATH_MESSAGES = registerBooleanRule("showDeathMessages", GameRules.Category.CHAT, true);
            RULE_SHOW_RECIPE_MESSAGES = registerBooleanRule("showRecipeMessages", GameRules.Category.CHAT, true);
            RULE_SHOW_TAGS = registerBooleanRule("showTags", GameRules.Category.MISC, false);
            RULE_SHOW_ACCUMULATION_HEIGHT = registerBooleanRule("showAccumulationHeight", GameRules.Category.MISC, false);

            RULE_MINECART_MAX_SPEED = registerIntRule("minecartMaxSpeed", GameRules.Category.MISC, 8, 0, 100);
            RULE_RANDOM_TICK_SPEED = registerIntRule("randomTickSpeed", GameRules.Category.UPDATES, 3, 0, 100);
            RULE_MAX_ENTITY_CRAMMING = registerIntRule("maxEntityCramming", GameRules.Category.MOBS, 24, 0, 100);
            RULE_SPAWN_CHUNK_RADIUS = registerIntRule("spawnChunkRadius", GameRules.Category.SPAWNING, 2, 2, 32);
            RULE_SPAWN_RADIUS = registerIntRule("spawnRadius", GameRules.Category.SPAWNING, 10, 0, 100);
        });
    }

    // 将辅助方法改为静态方法
    public static GameRules.Key<GameRules.BooleanValue> registerBooleanRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register("hwfsum_" + name, category, GameRules.BooleanValue.create(defaultValue));
    }

    public static GameRules.Key<GameRules.IntegerValue> registerIntRule(String name, GameRules.Category category, int defaultValue, int min, int max) {
        return GameRules.register("hwfsum_" + name, category, GameRules.IntegerValue.create(defaultValue, (server, value) -> {}));
    }
}