package com.heiwanfeng.hwfsum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ColorfulLampBlock extends Block {
    private final DyeColor color;
    private static final VoxelShape SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);
    
    public ColorfulLampBlock(DyeColor color) {
        super(BlockBehaviour.Properties.of()
            .mapColor(getMapColorFromDye(color))
            .strength(0.3F)
            .lightLevel(state -> 15) // 最大亮度
            .requiresCorrectToolForDrops()
        );
        this.color = color;
    }
    
    public DyeColor getColor() {
        return color;
    }
    
    private static MapColor getMapColorFromDye(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> MapColor.SNOW;
            case ORANGE -> MapColor.COLOR_ORANGE;
            case MAGENTA -> MapColor.COLOR_MAGENTA;
            case LIGHT_BLUE -> MapColor.COLOR_LIGHT_BLUE;
            case YELLOW -> MapColor.COLOR_YELLOW;
            case LIME -> MapColor.COLOR_LIGHT_GREEN;
            case PINK -> MapColor.COLOR_PINK;
            case GRAY -> MapColor.COLOR_GRAY;
            case LIGHT_GRAY -> MapColor.COLOR_LIGHT_GRAY;
            case CYAN -> MapColor.COLOR_CYAN;
            case PURPLE -> MapColor.COLOR_PURPLE;
            case BLUE -> MapColor.COLOR_BLUE;
            case BROWN -> MapColor.COLOR_BROWN;
            case GREEN -> MapColor.COLOR_GREEN;
            case RED -> MapColor.COLOR_RED;
            case BLACK -> MapColor.COLOR_BLACK;
        };
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}