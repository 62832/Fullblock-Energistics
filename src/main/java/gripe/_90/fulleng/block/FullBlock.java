package gripe._90.fulleng.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

import appeng.api.orientation.IOrientationStrategy;
import appeng.api.orientation.OrientationStrategies;
import appeng.block.AEBaseEntityBlock;

import gripe._90.fulleng.block.entity.FullBlockEntity;

public abstract class FullBlock<F extends FullBlockEntity> extends AEBaseEntityBlock<F> {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    private final ItemLike equivalentPart;

    public FullBlock(ItemLike equivalentPart) {
        super(BlockBehaviour.Properties.of()
                .strength(2.2f, 11.0f)
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(POWERED) ? 9 : 0));
        this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
        this.equivalentPart = equivalentPart;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    protected BlockState updateBlockStateFromBlockEntity(BlockState currentState, F be) {
        return super.updateBlockStateFromBlockEntity(currentState, be).setValue(POWERED, be.isActive());
    }

    @Override
    public IOrientationStrategy getOrientationStrategy() {
        return OrientationStrategies.full();
    }

    public ItemLike getEquivalentPart() {
        return equivalentPart;
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return equivalentPart.asItem().getDescriptionId();
    }
}
