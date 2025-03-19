package gripe._90.fulleng.block;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.util.Lazy;

import appeng.api.orientation.IOrientationStrategy;
import appeng.api.orientation.OrientationStrategies;
import appeng.block.AEBaseEntityBlock;
import appeng.core.definitions.AEItems;

import gripe._90.fulleng.block.entity.FullBlockEntity;

public abstract class FullBlock<F extends FullBlockEntity> extends AEBaseEntityBlock<F> {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    // Uses suppliers to avoid problems when attempting to retrieve a DeferredItem directly
    private final Supplier<ItemLike> equivalentPart;

    public FullBlock(Supplier<ItemLike> equivalentPart) {
        super(BlockBehaviour.Properties.of()
                .strength(2.2f, 11.0f)
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(POWERED) ? 9 : 0));
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
        this.equivalentPart = Lazy.of(equivalentPart);
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
        return equivalentPart.get();
    }

    public boolean shouldShowInCreative() {
        return equivalentPart.get() != AEItems.MISSING_CONTENT;
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return equivalentPart.get().asItem().getDescriptionId();
    }
}
