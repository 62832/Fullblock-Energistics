package gripe._90.fulleng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import appeng.block.AEBaseBlockItem;
import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.core.CreativeTab;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.PartItem;
import appeng.parts.reporting.AbstractDisplayPart;

import gripe._90.fulleng.block.RequesterTerminalBlock;
import gripe._90.fulleng.block.TerminalBlock;
import gripe._90.fulleng.block.entity.CraftingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.ItemTerminalBlockEntity;
import gripe._90.fulleng.block.entity.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.block.entity.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.RequesterTerminalBlockEntity;
import gripe._90.fulleng.block.entity.TerminalBlockEntity;

public final class FullblockEnergistics {
    public static final String MODID = "fulleng";
    public static final Platform PLATFORM = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    public static ResourceLocation makeId(String path) {
        return new ResourceLocation(MODID, path);
    }

    private static final List<BlockDefinition<?>> BLOCKS = new ArrayList<>();
    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITIES = new HashMap<>();

    public static List<BlockDefinition<?>> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static Map<ResourceLocation, BlockEntityType<?>> getBlockEntities() {
        return Collections.unmodifiableMap(BLOCK_ENTITIES);
    }

    // spotless:off
    public static final BlockBehaviour.Properties DEFAULT_PROPS = BlockBehaviour.Properties.of(Material.METAL).strength(2.2f, 11.0f).sound(SoundType.METAL);

    public static final BlockDefinition<TerminalBlock<ItemTerminalBlockEntity>> TERMINAL_BLOCK = terminal(AEParts.TERMINAL);
    public static final BlockDefinition<TerminalBlock<CraftingTerminalBlockEntity>> CRAFTING_TERMINAL_BLOCK = terminal(AEParts.CRAFTING_TERMINAL);
    public static final BlockDefinition<TerminalBlock<PatternEncodingTerminalBlockEntity>> PATTERN_ENCODING_TERMINAL_BLOCK = terminal(AEParts.PATTERN_ENCODING_TERMINAL);
    public static final BlockDefinition<TerminalBlock<PatternAccessTerminalBlockEntity>> PATTERN_ACCESS_TERMINAL_BLOCK = terminal(AEParts.PATTERN_ACCESS_TERMINAL);
    
    public static final BlockEntityType<ItemTerminalBlockEntity> TERMINAL = blockEntity("terminal", ItemTerminalBlockEntity.class, ItemTerminalBlockEntity::new, TERMINAL_BLOCK);
    public static final BlockEntityType<CraftingTerminalBlockEntity> CRAFTING_TERMINAL = blockEntity("crafting_terminal", CraftingTerminalBlockEntity.class, CraftingTerminalBlockEntity::new, CRAFTING_TERMINAL_BLOCK);
    public static final BlockEntityType<PatternEncodingTerminalBlockEntity> PATTERN_ENCODING_TERMINAL = blockEntity("pattern_encoding_terminal", PatternEncodingTerminalBlockEntity.class, PatternEncodingTerminalBlockEntity::new, PATTERN_ENCODING_TERMINAL_BLOCK);
    public static final BlockEntityType<PatternAccessTerminalBlockEntity> PATTERN_ACCESS_TERMINAL = blockEntity("pattern_access_terminal", PatternAccessTerminalBlockEntity.class, PatternAccessTerminalBlockEntity::new, PATTERN_ACCESS_TERMINAL_BLOCK);
    
    public static final BlockDefinition<TerminalBlock<RequesterTerminalBlockEntity>> REQUESTER_TERMINAL_BLOCK = block("ME Requester Terminal", "requester_terminal", RequesterTerminalBlock::new, RequesterTerminalBlock.BlockItem::new);
    public static final BlockEntityType<RequesterTerminalBlockEntity> REQUESTER_TERMINAL = blockEntity("requester_terminal", RequesterTerminalBlockEntity.class, RequesterTerminalBlockEntity::new, REQUESTER_TERMINAL_BLOCK);
    // spotless:on

    static <P extends AbstractDisplayPart, E extends TerminalBlockEntity> BlockDefinition<TerminalBlock<E>> terminal(
            ItemDefinition<PartItem<P>> equivalentPart) {
        return block(equivalentPart.getEnglishName(), equivalentPart.id().getPath(),
                () -> new TerminalBlock<>(equivalentPart));
    }

    static <T extends Block> BlockDefinition<T> block(String englishName, String id, Supplier<T> supplier) {
        return block(englishName, id, supplier,
                block -> new AEBaseBlockItem(block, new Item.Properties().tab(CreativeTab.INSTANCE)));
    }

    static <T extends Block> BlockDefinition<T> block(String englishName, String id, Supplier<T> blockSupplier,
            Function<T, ? extends AEBaseBlockItem> itemFunction) {
        var block = blockSupplier.get();
        var item = itemFunction.apply(block);
        var definition = new BlockDefinition<>(englishName, makeId(id), block, item);
        BLOCKS.add(definition);
        CreativeTab.add(definition);
        return definition;
    }

    static <T extends AEBaseBlockEntity> BlockEntityType<T> blockEntity(String id, Class<T> entityClass,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            BlockDefinition<? extends AEBaseEntityBlock<T>> block) {
        var type = BlockEntityType.Builder.of(supplier, block.block()).build(null);
        BLOCK_ENTITIES.put(makeId(id), type);
        AEBaseBlockEntity.registerBlockEntityItem(type, block.asItem());
        block.block().setBlockEntity(entityClass, type, null, null);
        return type;
    }

    public interface Platform {
        boolean isRequesterLoaded();
    }
}
