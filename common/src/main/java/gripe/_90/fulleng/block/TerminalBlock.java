package gripe._90.fulleng.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import appeng.util.InteractionUtil;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.terminal.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;

public class TerminalBlock<T extends TerminalBlockEntity> extends FullBlock<T> {
    public TerminalBlock(ItemLike equivalentPart) {
        super(equivalentPart);
    }

    @Override
    public InteractionResult onActivated(Level level, BlockPos pos, Player p, InteractionHand hand, ItemStack heldItem,
            BlockHitResult hit) {
        if (InteractionUtil.isInAlternateUseMode(p)) {
            return InteractionResult.PASS;
        }

        var be = getBlockEntity(level, pos);

        if (be != null) {
            if (!level.isClientSide()) {
                if (hit.getDirection().equals(be.getForward())) {
                    be.openMenu(p);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    protected boolean isInventory() {
        return this == FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BLOCK.block();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (getBlockEntity(level, pos) instanceof PatternEncodingTerminalBlockEntity patternTerminal)
                ? patternTerminal.getLogic().getBlankPatternInv().getRedstoneSignal()
                : 0;
    }
}
