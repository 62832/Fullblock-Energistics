package gripe._90.fulleng.block.entity.monitor;

import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.stacks.AEItemKey;
import appeng.api.storage.ISubMenuHost;
import appeng.api.storage.StorageHelper;
import appeng.me.helpers.PlayerSource;
import appeng.menu.ISubMenu;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.crafting.CraftAmountMenu;
import appeng.util.inv.PlayerInternalInventory;

import gripe._90.fulleng.FullblockEnergistics;

public class ConversionMonitorBlockEntity extends MonitorBlockEntity implements ISubMenuHost {
    public ConversionMonitorBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.CONVERSION_MONITOR, pos, blockState);
    }

    @Override
    public void onActivated(Player player, InteractionHand hand) {
        var eq = player.getItemInHand(hand);

        if (isLocked()) {
            insertItem(player, hand, eq.isEmpty());
        } else if (getDisplayed() != null && AEItemKey.matches(getDisplayed(), eq)) {
            insertItem(player, hand, false);
        } else {
            super.onActivated(player, hand);
        }
    }

    private void insertItem(Player player, InteractionHand hand, boolean allItems) {
        getMainNode().ifPresent(grid -> {
            var energy = grid.getEnergyService();
            var cell = grid.getStorageService().getInventory();

            if (allItems) {
                if (getDisplayed() instanceof AEItemKey itemKey) {
                    var inv = new PlayerInternalInventory(player.getInventory());

                    for (int x = 0; x < inv.size(); x++) {
                        var targetStack = inv.getStackInSlot(x);

                        if (itemKey.matches(targetStack)) {
                            var canExtract = inv.extractItem(x, targetStack.getCount(), true);

                            if (!canExtract.isEmpty()) {
                                var inserted = StorageHelper.poweredInsert(
                                        energy, cell, itemKey, canExtract.getCount(), new PlayerSource(player, this));
                                inv.extractItem(x, (int) inserted, false);
                            }
                        }
                    }
                }
            } else {
                var input = player.getItemInHand(hand);

                if (!input.isEmpty()) {
                    var inserted = StorageHelper.poweredInsert(
                            energy,
                            cell,
                            Objects.requireNonNull(AEItemKey.of(input)),
                            input.getCount(),
                            new PlayerSource(player, this));
                    input.shrink((int) inserted);
                }
            }
        });
    }

    public void extractItem(Player player, boolean shift) {
        if (!(getDisplayed() instanceof AEItemKey item)) {
            return;
        }

        if (!(getMainNode().isActive())) {
            return;
        }

        if (getAmount() == 0 && canCraft()) {
            CraftAmountMenu.open(
                    (ServerPlayer) player, MenuLocators.forBlockEntity(this), item, item.getAmountPerUnit());
            return;
        }

        getMainNode().ifPresent(grid -> {
            var count = shift ? 1 : item.getItem().getMaxStackSize();
            var retrieved = StorageHelper.poweredExtraction(
                    grid.getEnergyService(),
                    grid.getStorageService().getInventory(),
                    item,
                    count,
                    new PlayerSource(player, this));

            if (retrieved != 0) {
                var newItems = item.toStack((int) retrieved);

                if (!player.getInventory().add(newItems)) {
                    player.drop(newItems, false);
                }

                player.containerMenu.broadcastChanges();
            }
        });
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        player.closeContainer();
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(FullblockEnergistics.CONVERSION_MONITOR_BLOCK);
    }
}
