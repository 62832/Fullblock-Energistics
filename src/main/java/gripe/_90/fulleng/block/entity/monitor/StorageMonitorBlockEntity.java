package gripe._90.fulleng.block.entity.monitor;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.behaviors.ContainerItemStrategies;
import appeng.api.networking.IGrid;
import appeng.api.networking.IStackWatcher;
import appeng.api.networking.crafting.ICraftingWatcherNode;
import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AmountFormat;
import appeng.api.util.INetworkToolAware;
import appeng.core.localization.PlayerMessages;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.FullBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class StorageMonitorBlockEntity extends FullBlockEntity implements INetworkToolAware {
    @Nullable
    private AEKey configuredItem;

    private long amount;
    private boolean canCraft;
    private String lastHumanReadableText;
    private boolean isLocked;
    private IStackWatcher storageWatcher;
    private IStackWatcher craftingWatcher;

    public StorageMonitorBlockEntity(BlockPos pos, BlockState state) {
        this(FullblockEnergistics.STORAGE_MONITOR_BE.get(), pos, state);
    }

    public StorageMonitorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        getMainNode().addService(IStorageWatcherNode.class, new IStorageWatcherNode() {
            @Override
            public void updateWatcher(IStackWatcher newWatcher) {
                storageWatcher = newWatcher;
                configureWatchers();
            }

            @Override
            public void onStackChange(AEKey what, long amount) {
                if (what.equals(configuredItem)) {
                    StorageMonitorBlockEntity.this.amount = amount;

                    var humanReadableText =
                            amount == 0 && canCraft ? "Craft" : what.formatAmount(amount, AmountFormat.SLOT);

                    if (!humanReadableText.equals(lastHumanReadableText)) {
                        lastHumanReadableText = humanReadableText;
                        saveChanges();
                        markForUpdate();
                    }
                }
            }
        });

        getMainNode().addService(ICraftingWatcherNode.class, new ICraftingWatcherNode() {
            @Override
            public void updateWatcher(IStackWatcher newWatcher) {
                craftingWatcher = newWatcher;
                configureWatchers();
            }

            @Override
            public void onRequestChange(AEKey what) {}

            @Override
            public void onCraftableChange(AEKey what) {
                getMainNode().ifPresent(StorageMonitorBlockEntity.this::updateReportingValue);
            }
        });
    }

    @Override
    protected boolean readFromStream(RegistryFriendlyByteBuf data) {
        var needRedraw = super.readFromStream(data);
        var wasLocked = data.readBoolean();
        isLocked = wasLocked;

        if (data.readBoolean()) {
            configuredItem = AEKey.readKey(data);
            amount = data.readVarLong();
            canCraft = data.readBoolean();
        } else {
            configuredItem = null;
            amount = 0;
            canCraft = false;
        }

        return wasLocked != isLocked || needRedraw;
    }

    @Override
    protected void writeToStream(RegistryFriendlyByteBuf data) {
        super.writeToStream(data);
        data.writeBoolean(isLocked);
        data.writeBoolean(configuredItem != null);

        if (configuredItem != null) {
            AEKey.writeKey(data, configuredItem);
            data.writeVarLong(amount);
            data.writeBoolean(canCraft);
        }
    }

    @Override
    protected void saveVisualState(CompoundTag data) {
        super.saveVisualState(data);
        data.putLong("amount", amount);
        data.putBoolean("canCraft", canCraft);
    }

    @Override
    protected void loadVisualState(CompoundTag data) {
        super.loadVisualState(data);
        amount = data.getLong("amount");
        canCraft = data.getBoolean("canCraft");
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        data.putBoolean("isLocked", isLocked);

        if (configuredItem != null) {
            data.put("configuredItem", configuredItem.toTagGeneric(registries));
        }
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        isLocked = data.getBoolean("isLocked");
        configuredItem = AEKey.fromTagGeneric(registries, data.getCompound("configuredItem"));
    }

    public void onActivated(Player player, InteractionHand hand) {
        if (!isLocked) {
            var eq = player.getItemInHand(hand);

            if (AEItemKey.matches(configuredItem, eq)) {
                var containedStack = ContainerItemStrategies.getContainedStack(eq);

                if (containedStack != null) {
                    configuredItem = containedStack.what();
                }
            } else {
                configuredItem = AEItemKey.of(eq);
            }

            configureWatchers();
            saveChanges();
            markForUpdate();
        }
    }

    public void onShiftActivated(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            isLocked = !isLocked;
            player.sendSystemMessage((isLocked ? PlayerMessages.isNowLocked : PlayerMessages.isNowUnlocked).text());
            saveChanges();
            markForUpdate();
        }
    }

    private void configureWatchers() {
        if (storageWatcher != null) {
            storageWatcher.reset();
        }

        if (craftingWatcher != null) {
            craftingWatcher.reset();
        }

        if (configuredItem != null) {
            if (storageWatcher != null) {
                storageWatcher.add(configuredItem);
            }

            if (craftingWatcher != null) {
                craftingWatcher.add(configuredItem);
            }

            getMainNode().ifPresent(this::updateReportingValue);
        }
    }

    private void updateReportingValue(IGrid grid) {
        lastHumanReadableText = null;

        if (configuredItem != null) {
            amount = grid.getStorageService().getCachedInventory().get(configuredItem);
            canCraft = grid.getCraftingService().isCraftable(configuredItem);
        } else {
            amount = 0;
            canCraft = false;
        }

        markForUpdate();
    }

    public AEKey getDisplayed() {
        return configuredItem;
    }

    public long getAmount() {
        return amount;
    }

    public boolean canCraft() {
        return canCraft;
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public boolean showNetworkInfo(UseOnContext context) {
        return context.getClickedFace() != getFront();
    }
}
