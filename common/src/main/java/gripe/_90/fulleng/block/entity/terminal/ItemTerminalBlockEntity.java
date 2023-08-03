package gripe._90.fulleng.block.entity.terminal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.fulleng.definition.FullEngBlockEntities;

public class ItemTerminalBlockEntity extends StorageTerminalBlockEntity {
    public ItemTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBlockEntities.TERMINAL, pos, blockState);
    }
}
