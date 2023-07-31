package gripe._90.fulleng.block.entity.terminal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.util.ConfigManager;

import gripe._90.fulleng.block.entity.FullBlockEntity;

public abstract class TerminalBlockEntity extends FullBlockEntity implements IConfigurableObject {
    private final IConfigManager cm = new ConfigManager(this::saveChanges);

    public TerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        cm.writeToNBT(data);
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        cm.readFromNBT(data);
    }

    public abstract MenuType<?> getMenuType(Player player);

    public void openMenu(Player player) {
        MenuOpener.open(getMenuType(player), player, MenuLocators.forBlockEntity(this));
    }

    @Override
    public IConfigManager getConfigManager() {
        return cm;
    }
}
