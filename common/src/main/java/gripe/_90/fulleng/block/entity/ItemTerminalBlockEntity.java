package gripe._90.fulleng.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.fulleng.FullblockEnergistics;

public class ItemTerminalBlockEntity extends StorageTerminalBlockEntity {
    public ItemTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.TERMINAL, pos, blockState);
    }
}
