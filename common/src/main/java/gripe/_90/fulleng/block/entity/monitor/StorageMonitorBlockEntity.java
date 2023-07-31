package gripe._90.fulleng.block.entity.monitor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.fulleng.FullblockEnergistics;

public class StorageMonitorBlockEntity extends MonitorBlockEntity {
    public StorageMonitorBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.STORAGE_MONITOR, pos, blockState);
    }
}
