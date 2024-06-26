package gripe._90.fulleng.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import appeng.core.definitions.ItemDefinition;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.block.entity.terminal.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;
import gripe._90.fulleng.definition.FullEngBlocks;

public class TerminalBlock<T extends TerminalBlockEntity> extends FullBlock<T> {
    public TerminalBlock(ItemDefinition<?> equivalentPart) {
        super(equivalentPart);
    }

    @Override
    public InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (InteractionUtil.isInAlternateUseMode(player)) {
            return InteractionResult.PASS;
        }

        var be = getBlockEntity(level, pos);

        if (be != null) {
            if (!level.isClientSide()) {
                if (hit.getDirection().equals(be.getFront())) {
                    MenuOpener.open(be.getMenuType(), player, MenuLocators.forBlockEntity(be));
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return this == FullEngBlocks.PATTERN_ENCODING_TERMINAL.block();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (getBlockEntity(level, pos) instanceof PatternEncodingTerminalBlockEntity patternTerminal)
                ? patternTerminal.getLogic().getBlankPatternInv().getRedstoneSignal()
                : 0;
    }
}
