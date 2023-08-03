package gripe._90.fulleng.block.entity.monitor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.fulleng.definition.FullEngBlockEntities;

public class StorageMonitorBlockEntity extends MonitorBlockEntity {
    public StorageMonitorBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBlockEntities.STORAGE_MONITOR, pos, blockState);
    }
}
