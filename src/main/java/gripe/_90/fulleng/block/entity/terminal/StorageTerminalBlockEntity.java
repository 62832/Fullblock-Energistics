package gripe._90.fulleng.block.entity.terminal;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import appeng.api.config.ViewItems;
import appeng.api.implementations.blockentities.IViewCellStorage;
import appeng.api.inventories.InternalInventory;
import appeng.api.storage.ILinkStatus;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.api.storage.SupplierStorage;
import appeng.api.util.IConfigManagerBuilder;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.me.common.MEStorageMenu;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;

import gripe._90.fulleng.FullblockEnergistics;

public class StorageTerminalBlockEntity extends TerminalBlockEntity
        implements ITerminalHost, IViewCellStorage, InternalInventoryHost {
    private final AppEngInternalInventory viewCell = new AppEngInternalInventory(this, 5);

    public StorageTerminalBlockEntity(BlockPos pos, BlockState state) {
        this(FullblockEnergistics.TERMINAL_BE.get(), pos, state);
    }

    public StorageTerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void registerSettings(IConfigManagerBuilder builder) {
        builder.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        builder.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        builder.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        viewCell.writeToNBT(data, "viewCell", registries);
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        viewCell.readFromNBT(data, "viewCell", registries);
    }

    @Override
    public InternalInventory getViewCellStorage() {
        return viewCell;
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        super.addAdditionalDrops(level, pos, drops);

        for (var is : viewCell) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
    }

    @Override
    public MEStorage getInventory() {
        return new SupplierStorage(() -> {
            var grid = getMainNode().getGrid();
            return grid != null ? grid.getStorageService().getInventory() : null;
        });
    }

    @Override
    public void saveChangedInventory(AppEngInternalInventory inv) {
        markForUpdate();
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(getItemFromBlockEntity());
    }

    @Override
    public MenuType<?> getMenuType() {
        return MEStorageMenu.TYPE;
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.open(getMenuType(), player, subMenu.getLocator(), true);
    }

    @Override
    public ILinkStatus getLinkStatus() {
        return ILinkStatus.ofManagedNode(getMainNode());
    }
}
