package gripe._90.fulleng.block.entity;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.TypeFilter;
import appeng.api.config.ViewItems;
import appeng.api.implementations.blockentities.IViewCellStorage;
import appeng.api.inventories.InternalInventory;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.me.common.MEStorageMenu;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;

public abstract class StorageTerminalBlockEntity extends TerminalBlockEntity
        implements ITerminalHost, IViewCellStorage, InternalInventoryHost {
    private final AppEngInternalInventory viewCell = new AppEngInternalInventory(this, 5);

    public StorageTerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.getConfigManager().registerSetting(Settings.SORT_BY, SortOrder.NAME);
        this.getConfigManager().registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        this.getConfigManager().registerSetting(Settings.TYPE_FILTER, TypeFilter.ALL);
        this.getConfigManager().registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        viewCell.writeToNBT(data, "viewCell");
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        viewCell.readFromNBT(data, "viewCell");
    }

    @Override
    public InternalInventory getViewCellStorage() {
        return viewCell;
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        for (var is : viewCell) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
    }

    @Override
    public MEStorage getInventory() {
        var grid = getMainNode().getGrid();
        return grid != null ? grid.getStorageService().getInventory() : null;
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(getItemFromBlockEntity());
    }

    @Override
    public void onChangeInventory(InternalInventory inv, int slot) {
        markForUpdate();
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return MEStorageMenu.TYPE;
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.open(getMenuType(player), player, subMenu.getLocator(), true);
    }
}
