package gripe._90.fulleng.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;

import appeng.block.AEBaseEntityBlock;
import appeng.core.definitions.ItemDefinition;

import gripe._90.fulleng.block.entity.FullBlockEntity;

public abstract class FullBlock<F extends FullBlockEntity> extends AEBaseEntityBlock<F> {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    private final ItemDefinition<?> equivalentPart;

    public FullBlock(ItemDefinition<?> equivalentPart) {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(2.2f, 11.0f).sound(SoundType.METAL)
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

    public ItemDefinition<?> getEquivalentPart() {
        return equivalentPart;
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return equivalentPart.asItem().getDescriptionId();
    }
}
