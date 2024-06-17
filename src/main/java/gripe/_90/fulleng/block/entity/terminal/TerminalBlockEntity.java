package gripe._90.fulleng.block.entity.terminal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigManagerBuilder;
import appeng.api.util.IConfigurableObject;

import gripe._90.fulleng.block.entity.FullBlockEntity;

public abstract class TerminalBlockEntity extends FullBlockEntity implements IConfigurableObject {
    private final IConfigManager cm;

    public TerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        var builder = IConfigManager.builder(this::saveChanges);
        registerSettings(builder);
        cm = builder.build();
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        cm.writeToNBT(data, registries);
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        cm.readFromNBT(data, registries);
    }

    public abstract MenuType<?> getMenuType();

    protected void registerSettings(IConfigManagerBuilder builder) {}

    @Override
    public IConfigManager getConfigManager() {
        return cm;
    }
}
