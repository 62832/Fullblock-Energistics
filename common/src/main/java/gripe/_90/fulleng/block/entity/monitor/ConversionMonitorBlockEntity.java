package gripe._90.fulleng.block.entity.monitor;

import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.stacks.AEItemKey;
import appeng.api.storage.StorageHelper;
import appeng.me.helpers.PlayerSource;
import appeng.util.inv.PlayerInternalInventory;

import gripe._90.fulleng.FullblockEnergistics;

public class ConversionMonitorBlockEntity extends MonitorBlockEntity {
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

    public void onClicked(Player player) {
        if (getDisplayed() instanceof AEItemKey item) {
            extractItem(player, item.getItem().getMaxStackSize());
        }
    }

    public void onShiftClicked(Player player) {
        if (getDisplayed() instanceof AEItemKey) {
            extractItem(player, 1);
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
                                var inserted = StorageHelper.poweredInsert(energy, cell, itemKey, canExtract.getCount(),
                                        new PlayerSource(player, this));
                                inv.extractItem(x, (int) inserted, false);
                            }
                        }
                    }
                }
            } else {
                var input = player.getItemInHand(hand);

                if (!input.isEmpty()) {
                    var inserted = StorageHelper.poweredInsert(energy, cell,
                            Objects.requireNonNull(AEItemKey.of(input)),
                            input.getCount(), new PlayerSource(player, this));
                    input.shrink((int) inserted);
                }
            }
        });
    }

    private void extractItem(Player player, int count) {
        if (!(this.getDisplayed() instanceof AEItemKey itemKey)) {
            return;
        }

        getMainNode().ifPresent(grid -> {
            var retrieved = StorageHelper.poweredExtraction(grid.getEnergyService(),
                    grid.getStorageService().getInventory(), itemKey, count, new PlayerSource(player, this));

            if (retrieved != 0) {
                var newItems = itemKey.toStack((int) retrieved);

                if (!player.getInventory().add(newItems)) {
                    player.drop(newItems, false);
                }

                player.containerMenu.broadcastChanges();
            }
        });
    }
}
