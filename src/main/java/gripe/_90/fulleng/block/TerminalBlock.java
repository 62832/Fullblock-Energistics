package gripe._90.fulleng.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.terminal.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;

public class TerminalBlock<T extends TerminalBlockEntity> extends FullBlock<T> {
    public TerminalBlock(ItemLike equivalentPart) {
        super(equivalentPart);
    }

    @NotNull
    @Override
    public InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        var be = getBlockEntity(level, pos);

        if (be != null) {
            if (!level.isClientSide()) {
                if (hit.getDirection().equals(be.getFront()) && be.getMenuType() != null) {
                    MenuOpener.open(be.getMenuType(), player, MenuLocators.forBlockEntity(be));
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return state.is(FullblockEnergistics.PATTERN_ENCODING_TERMINAL.get());
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (getBlockEntity(level, pos) instanceof PatternEncodingTerminalBlockEntity patternTerminal)
                ? patternTerminal.getLogic().getBlankPatternInv().getRedstoneSignal()
                : 0;
    }
}
