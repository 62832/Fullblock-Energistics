package gripe._90.fulleng;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import appeng.api.AECapabilities;
import appeng.api.ids.AECreativeTabIds;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.block.AEBaseBlockItem;
import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.PartItem;
import appeng.parts.reporting.AbstractDisplayPart;
import appeng.parts.reporting.AbstractMonitorPart;

import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.block.TerminalBlock;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;
import gripe._90.fulleng.block.entity.terminal.CraftingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.StorageTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;
import gripe._90.fulleng.integration.Addons;
import gripe._90.fulleng.integration.IntegrationBlockItem;
import gripe._90.fulleng.integration.appliede.AppliedEIntegration;
import gripe._90.fulleng.integration.appliede.TransmutationTerminalBlockEntity;
import gripe._90.fulleng.integration.extendedae.ExtendedAEIntegration;
import gripe._90.fulleng.integration.extendedae.ExtendedPatternAccessTerminalBlockEntity;
import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.integration.requester.RequesterTerminalBlockEntity;

@Mod(FullblockEnergistics.MODID)
public class FullblockEnergistics {
    public static final String MODID = "fulleng";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // spotless:off
    public static final DeferredBlock<TerminalBlock<StorageTerminalBlockEntity>> TERMINAL = terminal(AEParts.TERMINAL);
    public static final DeferredBlock<TerminalBlock<CraftingTerminalBlockEntity>> CRAFTING_TERMINAL = terminal(AEParts.CRAFTING_TERMINAL);
    public static final DeferredBlock<TerminalBlock<PatternEncodingTerminalBlockEntity>> PATTERN_ENCODING_TERMINAL = terminal(AEParts.PATTERN_ENCODING_TERMINAL);
    public static final DeferredBlock<TerminalBlock<PatternAccessTerminalBlockEntity>> PATTERN_ACCESS_TERMINAL = terminal(AEParts.PATTERN_ACCESS_TERMINAL);

    public static final DeferredBlock<MonitorBlock<StorageMonitorBlockEntity>> STORAGE_MONITOR = monitor(AEParts.STORAGE_MONITOR);
    public static final DeferredBlock<MonitorBlock<ConversionMonitorBlockEntity>> CONVERSION_MONITOR = monitor(AEParts.CONVERSION_MONITOR);

    public static final DeferredBlock<TerminalBlock<RequesterTerminalBlockEntity>> REQUESTER_TERMINAL = terminal("requester_terminal", Addons.MEREQUESTER, () -> RequesterIntegration.REQUESTER_TERMINAL);
    public static final DeferredBlock<TerminalBlock<ExtendedPatternAccessTerminalBlockEntity>> EXTENDED_PATTERN_ACCESS_TERMINAL = terminal("extended_pattern_access_terminal", Addons.EXTENDEDAE, () -> ExtendedAEIntegration.EXTENDED_PATTERN_TERMINAL);
    public static final DeferredBlock<TerminalBlock<TransmutationTerminalBlockEntity>> TRANSMUTATION_TERMINAL = terminal("transmutation_terminal", Addons.APPLIEDE, () -> AppliedEIntegration.TRANSMUTATION_TERMINAL);
    // spotless:on

    private static final DeferredRegister<BlockEntityType<?>> BE_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    // spotless:off
    public static final Supplier<BlockEntityType<StorageTerminalBlockEntity>> TERMINAL_BE = be(StorageTerminalBlockEntity.class, StorageTerminalBlockEntity::new, TERMINAL);
    public static final Supplier<BlockEntityType<CraftingTerminalBlockEntity>> CRAFTING_TERMINAL_BE = be(CraftingTerminalBlockEntity.class, CraftingTerminalBlockEntity::new, CRAFTING_TERMINAL);
    public static final Supplier<BlockEntityType<PatternEncodingTerminalBlockEntity>> PATTERN_ENCODING_TERMINAL_BE = be(PatternEncodingTerminalBlockEntity.class, PatternEncodingTerminalBlockEntity::new, PATTERN_ENCODING_TERMINAL);
    public static final Supplier<BlockEntityType<PatternAccessTerminalBlockEntity>> PATTERN_ACCESS_TERMINAL_BE = be(PatternAccessTerminalBlockEntity.class, PatternAccessTerminalBlockEntity::new, PATTERN_ACCESS_TERMINAL);

    public static final Supplier<BlockEntityType<StorageMonitorBlockEntity>> STORAGE_MONITOR_BE = be(StorageMonitorBlockEntity.class, StorageMonitorBlockEntity::new, STORAGE_MONITOR);
    public static final Supplier<BlockEntityType<ConversionMonitorBlockEntity>> CONVERSION_MONITOR_BE = be(ConversionMonitorBlockEntity.class, ConversionMonitorBlockEntity::new, CONVERSION_MONITOR);

    public static final Supplier<BlockEntityType<RequesterTerminalBlockEntity>> REQUESTER_TERMINAL_BE = be(RequesterTerminalBlockEntity.class, RequesterTerminalBlockEntity::new, REQUESTER_TERMINAL);
    public static final Supplier<BlockEntityType<ExtendedPatternAccessTerminalBlockEntity>> EXTENDED_PATTERN_ACCESS_TERMINAL_BE = be(ExtendedPatternAccessTerminalBlockEntity.class, ExtendedPatternAccessTerminalBlockEntity::new, EXTENDED_PATTERN_ACCESS_TERMINAL);
    public static final Supplier<BlockEntityType<TransmutationTerminalBlockEntity>> TRANSMUTATION_TERMINAL_BE = be(TransmutationTerminalBlockEntity.class, TransmutationTerminalBlockEntity::new, TRANSMUTATION_TERMINAL);
    // spotless:on

    public FullblockEnergistics(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BE_TYPES.register(eventBus);

        eventBus.addListener(RegisterCapabilitiesEvent.class, event -> {
            for (var type : BE_TYPES.getEntries()) {
                event.registerBlockEntity(
                        AECapabilities.IN_WORLD_GRID_NODE_HOST, type.get(), (be, context) -> (IInWorldGridNodeHost) be);
            }

            event.registerBlockEntity(
                    Capabilities.ItemHandler.BLOCK,
                    PATTERN_ENCODING_TERMINAL_BE.get(),
                    (be, context) -> context != Direction.NORTH
                            ? be.getLogic().getBlankPatternInv().toItemHandler()
                            : null);
        });

        eventBus.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
            if (event.getTabKey().equals(AECreativeTabIds.MAIN)) {
                for (var block : BLOCKS.getEntries()) {
                    if (block.get() instanceof FullBlock<?> fullBlock && fullBlock.shouldShowInCreative()) {
                        event.accept(fullBlock);
                    }
                }
            }
        });
    }

    private static <P extends AbstractDisplayPart, E extends TerminalBlockEntity>
            DeferredBlock<TerminalBlock<E>> terminal(ItemDefinition<PartItem<P>> equivalentPart) {
        return block(equivalentPart.id().getPath(), () -> new TerminalBlock<>(() -> equivalentPart));
    }

    private static <P extends AbstractMonitorPart, E extends StorageMonitorBlockEntity>
            DeferredBlock<MonitorBlock<E>> monitor(ItemDefinition<PartItem<P>> equivalentPart) {
        return block(equivalentPart.id().getPath(), () -> new MonitorBlock<>(() -> equivalentPart));
    }

    private static <E extends TerminalBlockEntity> DeferredBlock<TerminalBlock<E>> terminal(
            String id, Addons addon, Supplier<ItemLike> equivalentPart) {
        return block(
                id,
                () -> new TerminalBlock<>(addon.isLoaded() ? equivalentPart : () -> AEItems.MISSING_CONTENT),
                block -> new IntegrationBlockItem(block, addon));
    }

    private static <T extends Block> DeferredBlock<T> block(String id, Supplier<T> supplier) {
        return block(id, supplier, block -> new AEBaseBlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> block(
            String id, Supplier<T> blockSupplier, Function<T, ? extends AEBaseBlockItem> itemFunction) {
        var block = BLOCKS.register(id, blockSupplier);
        ITEMS.register(id, () -> itemFunction.apply(block.get()));
        return block;
    }

    @SuppressWarnings("DataFlowIssue")
    private static <T extends AEBaseBlockEntity> Supplier<BlockEntityType<T>> be(
            Class<T> entityClass,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            DeferredBlock<? extends AEBaseEntityBlock<T>> block) {
        return BE_TYPES.register(block.getId().getPath(), () -> {
            var type = BlockEntityType.Builder.of(supplier, block.get()).build(null);
            AEBaseBlockEntity.registerBlockEntityItem(type, block.get().asItem());
            block.get().setBlockEntity(entityClass, type, null, null);
            return type;
        });
    }
}
