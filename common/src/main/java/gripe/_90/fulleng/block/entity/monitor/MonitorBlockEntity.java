package gripe._90.fulleng.block.entity.monitor;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.networking.IStackWatcher;
import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AmountFormat;
import appeng.api.util.INetworkToolAware;
import appeng.core.localization.PlayerMessages;
import appeng.menu.me.interaction.StackInteractions;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.block.entity.FullBlockEntity;

public abstract class MonitorBlockEntity extends FullBlockEntity
        implements IStorageWatcherNode, INetworkToolAware {
    @Nullable
    private AEKey configuredItem;
    private long amount;
    private String lastHumanReadableText;
    private boolean isLocked;
    private IStackWatcher stackWatcher;
    private byte spin = 0;

    public MonitorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.getMainNode().addService(IStorageWatcherNode.class, this);
    }

    @Override
    protected boolean readFromStream(FriendlyByteBuf data) {
        var needRedraw = super.readFromStream(data);
        var oldSpin = data.readByte();
        spin = oldSpin;

        var wasLocked = data.readBoolean();
        isLocked = wasLocked;

        if (data.readBoolean()) {
            configuredItem = AEKey.readKey(data);
            amount = data.readVarLong();
        } else {
            configuredItem = null;
            amount = 0;
        }

        return oldSpin != spin || wasLocked != isLocked || needRedraw;
    }

    @Override
    protected void writeToStream(FriendlyByteBuf data) {
        super.writeToStream(data);
        data.writeByte(spin);
        data.writeBoolean(isLocked);
        data.writeBoolean(configuredItem != null);

        if (configuredItem != null) {
            AEKey.writeKey(data, configuredItem);
            data.writeVarLong(amount);
        }
    }

    @Override
    protected void saveVisualState(CompoundTag data) {
        super.saveVisualState(data);
        data.putLong("amount", amount);
    }

    @Override
    protected void loadVisualState(CompoundTag data) {
        super.loadVisualState(data);
        amount = data.getLong("amount");
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        data.putByte("spin", spin);
        data.putBoolean("isLocked", isLocked);

        if (configuredItem != null) {
            data.put("configuredItem", configuredItem.toTagGeneric());
        }
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        spin = data.getByte("spin");
        isLocked = data.getBoolean("isLocked");
        configuredItem = AEKey.fromTagGeneric(data.getCompound("configuredItem"));
    }

    public void onActivated(Player player, InteractionHand hand) {
        if (!isLocked) {
            var eq = player.getItemInHand(hand);

            if (AEItemKey.matches(configuredItem, eq)) {
                var containedStack = StackInteractions.getContainedStack(eq);

                if (containedStack != null) {
                    configuredItem = containedStack.what();
                }
            } else {
                configuredItem = AEItemKey.of(eq);
            }

            configureWatchers();
        } else {
            if (InteractionUtil.canWrenchRotate(player.getInventory().getSelected())) {
                if (!isClientSide()) {
                    spin = (byte) ((spin + 1) % 4);
                }
            }
        }

        markForUpdate();
    }

    public void onShiftActivated(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            isLocked = !isLocked;
            player.sendSystemMessage((isLocked ? PlayerMessages.isNowLocked : PlayerMessages.isNowUnlocked).text());
            markForUpdate();
        }
    }

    public void onClicked(Player player) {
    }

    public void onShiftClicked(Player player) {
    }

    private void configureWatchers() {
        if (stackWatcher != null) {
            stackWatcher.reset();
        }

        if (configuredItem != null) {
            if (stackWatcher != null) {
                stackWatcher.add(configuredItem);
            }

            getMainNode().ifPresent(grid -> {
                lastHumanReadableText = null;

                if (configuredItem != null) {
                    amount = grid.getStorageService().getCachedInventory().get(configuredItem);
                } else {
                    amount = 0;
                }
            });
        }
    }

    @Override
    public void updateWatcher(IStackWatcher newWatcher) {
        stackWatcher = newWatcher;
        configureWatchers();
    }

    @Override
    public void onStackChange(AEKey what, long amount) {
        if (what.equals(configuredItem)) {
            this.amount = amount;

            var humanReadableText = what.formatAmount(amount, AmountFormat.SLOT);

            if (!humanReadableText.equals(lastHumanReadableText)) {
                lastHumanReadableText = humanReadableText;
                markForUpdate();
            }
        }
    }

    public AEKey getDisplayed() {
        return configuredItem;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public byte getSpin() {
        return spin;
    }

    @Override
    public boolean showNetworkInfo(UseOnContext context) {
        return false;
    }
}
