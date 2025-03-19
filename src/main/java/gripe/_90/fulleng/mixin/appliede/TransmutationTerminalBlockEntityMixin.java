package gripe._90.fulleng.mixin.appliede;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.appliede.me.misc.TransmutationTerminalHost;
import gripe._90.fulleng.block.entity.terminal.StorageTerminalBlockEntity;
import gripe._90.fulleng.integration.appliede.TransmutationTerminalBlockEntity;

@Mixin(TransmutationTerminalBlockEntity.class)
public abstract class TransmutationTerminalBlockEntityMixin extends StorageTerminalBlockEntity
        implements TransmutationTerminalHost {
    public TransmutationTerminalBlockEntityMixin(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Unique
    private boolean fulleng$shiftToTransmute;

    @Override
    public void setShiftToTransmute(boolean shift) {
        fulleng$shiftToTransmute = shift;
        markForUpdate();
    }

    @Override
    public boolean getShiftToTransmute() {
        return fulleng$shiftToTransmute;
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        data.putBoolean("shiftToTransmute", fulleng$shiftToTransmute);
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        fulleng$shiftToTransmute = data.getBoolean("shiftToTransmute");
    }
}
