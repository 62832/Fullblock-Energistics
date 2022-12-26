package gripe._90.fulleng.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.implementations.blockentities.IColorableBlockEntity;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNodeListener;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.blockentity.grid.AENetworkBlockEntity;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.util.ConfigManager;

public abstract class TerminalBlockEntity extends AENetworkBlockEntity
        implements IConfigurableObject, IColorableBlockEntity {
    private final IConfigManager cm = new ConfigManager(this::saveChanges);
    private AEColor paintedColour = AEColor.TRANSPARENT;
    private boolean isActive = false;

    public TerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.getMainNode().setFlags(GridFlags.REQUIRE_CHANNEL).setIdlePowerUsage(0.5);
    }

    @Override
    protected boolean readFromStream(FriendlyByteBuf data) {
        final boolean c = super.readFromStream(data);
        final boolean wasActive = this.isActive;
        this.isActive = data.readBoolean();

        final AEColor oldPaintedColor = this.paintedColour;
        this.paintedColour = AEColor.values()[data.readByte()];

        return oldPaintedColor != this.paintedColour || wasActive != this.isActive || c;
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
        cm.writeToNBT(data);
        data.putByte("paintedColor", (byte) paintedColour.ordinal());
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        cm.readFromNBT(data);
        if (data.contains("paintedColor")) {
            paintedColour = AEColor.values()[data.getByte("paintedColor")];
        }
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        if (reason != IGridNodeListener.State.GRID_BOOT) {
            this.markForUpdate();
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
            this.isActive = true;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.isActive = false;
    }

    public boolean isActive() {
        if (isClientSide()) {
            return isActive;
        } else {
            return getMainNode().isOnline();
        }
    }

    public abstract MenuType<?> getMenuType(Player player);

    public void openMenu(Player player) {
        MenuOpener.open(getMenuType(player), player, MenuLocators.forBlockEntity(this));
    }

    @Override
    public IConfigManager getConfigManager() {
        return cm;
    }

    @Override
    public AEColor getColor() {
        return paintedColour;
    }

    @Override
    public boolean recolourBlock(Direction side, AEColor colour, Player who) {
        if (this.paintedColour == colour) {
            return false;
        }

        this.paintedColour = colour;
        this.saveChanges();
        this.markForUpdate();
        return true;
    }
}
