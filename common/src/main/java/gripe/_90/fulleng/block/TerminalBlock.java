package gripe._90.fulleng.block;

import org.jetbrains.annotations.NotNull;

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

import appeng.block.AEBaseEntityBlock;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.TerminalBlockEntity;

public class TerminalBlock<T extends TerminalBlockEntity> extends AEBaseEntityBlock<T> {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    private final ItemLike equivalentPart;

    public TerminalBlock(ItemLike equivalentPart) {
        super(FullblockEnergistics.DEFAULT_PROPS);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
        this.equivalentPart = equivalentPart;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    protected BlockState updateBlockStateFromBlockEntity(BlockState currentState, TerminalBlockEntity be) {
        return currentState.setValue(POWERED, be.isActive());
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
                be.openMenu(p);
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
        return (getBlockEntity(level, pos)instanceof PatternEncodingTerminalBlockEntity patternTerminal)
                ? patternTerminal.getLogic().getBlankPatternInv().getRedstoneSignal()
                : 0;
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return equivalentPart.asItem().getDescriptionId();
    }
}
