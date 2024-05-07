package gripe._90.fulleng.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.util.DimensionalBlockPos;
import appeng.util.InteractionUtil;
import appeng.util.Platform;

import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;

public class MonitorBlock<M extends StorageMonitorBlockEntity> extends FullBlock<M> {
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    public MonitorBlock(ItemLike equivalentPart) {
        super(equivalentPart);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LOCKED);
    }

    @Override
    protected BlockState updateBlockStateFromBlockEntity(BlockState currentState, M be) {
        return super.updateBlockStateFromBlockEntity(currentState, be).setValue(LOCKED, be.isLocked());
    }

    @Override
    public InteractionResult onActivated(
            Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack heldItem, BlockHitResult hit) {
        var be = getBlockEntity(level, pos);

        if (be != null) {
            if (!level.isClientSide()) {
                if (hit.getDirection().equals(be.getFront())) {
                    if (be.getMainNode().isActive() && Platform.hasPermissions(new DimensionalBlockPos(be), player)) {
                        if (InteractionUtil.isInAlternateUseMode(player)) {
                            be.onShiftActivated(player, hand);
                        } else {
                            be.onActivated(player, hand);
                        }
                    }
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}
