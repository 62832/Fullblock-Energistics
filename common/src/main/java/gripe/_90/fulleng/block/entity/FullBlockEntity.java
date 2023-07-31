package gripe._90.fulleng.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.implementations.blockentities.IColorableBlockEntity;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNodeListener;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.blockentity.grid.AENetworkBlockEntity;

public abstract class FullBlockEntity extends AENetworkBlockEntity implements IColorableBlockEntity {
    private AEColor paintedColour = AEColor.TRANSPARENT;
    private boolean isActive = false;

    public FullBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.getMainNode().setFlags(GridFlags.REQUIRE_CHANNEL).setIdlePowerUsage(0.5);
    }

    @Override
    protected boolean readFromStream(FriendlyByteBuf data) {
        var needRedraw = super.readFromStream(data);
        var wasActive = isActive;
        isActive = data.readBoolean();

        var oldPaintedColor = paintedColour;
        paintedColour = AEColor.values()[data.readByte()];

        return oldPaintedColor != paintedColour || wasActive != isActive || needRedraw;
    }

    @Override
    protected void writeToStream(FriendlyByteBuf data) {
        super.writeToStream(data);
        data.writeBoolean(getMainNode().isOnline());
        data.writeByte(paintedColour.ordinal());
    }

    @Override
    protected void saveVisualState(CompoundTag data) {
        super.saveVisualState(data);
        data.putBoolean("active", isActive);
    }

    @Override
    protected void loadVisualState(CompoundTag data) {
        super.loadVisualState(data);
        isActive = data.getBoolean("active");
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        data.putByte("paintedColor", (byte) paintedColour.ordinal());
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);

        if (data.contains("paintedColor")) {
            paintedColour = AEColor.values()[data.getByte("paintedColor")];
        }
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        if (reason != IGridNodeListener.State.GRID_BOOT) {
            markForUpdate();
        }
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.COVERED;
    }

    @Override
    public void onReady() {
        super.onReady();

        if (!isClientSide()) {
            isActive = true;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        isActive = false;
    }

    public boolean isActive() {
        return isClientSide() ? isActive : getMainNode().isOnline();
    }

    @Override
    public AEColor getColor() {
        return paintedColour;
    }

    @Override
    public boolean recolourBlock(Direction side, AEColor colour, Player who) {
        if (paintedColour == colour) {
            return false;
        }

        paintedColour = colour;
        saveChanges();
        markForUpdate();
        return true;
    }
}
