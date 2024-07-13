package gripe._90.fulleng.definition;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import appeng.block.AEBaseBlockItem;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.PartItem;
import appeng.parts.reporting.AbstractDisplayPart;
import appeng.parts.reporting.AbstractMonitorPart;

import gripe._90.fulleng.FullblockEnergistics;
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
import gripe._90.fulleng.integration.requester.RequesterTerminalBlock;
import gripe._90.fulleng.integration.requester.RequesterTerminalBlockEntity;

public class FullEngBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FullblockEnergistics.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FullblockEnergistics.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    // spotless:off
    public static final DeferredBlock<TerminalBlock<StorageTerminalBlockEntity>> TERMINAL = terminal(AEParts.TERMINAL);
    public static final DeferredBlock<TerminalBlock<CraftingTerminalBlockEntity>> CRAFTING_TERMINAL = terminal(AEParts.CRAFTING_TERMINAL);
    public static final DeferredBlock<TerminalBlock<PatternEncodingTerminalBlockEntity>> PATTERN_ENCODING_TERMINAL = terminal(AEParts.PATTERN_ENCODING_TERMINAL);
    public static final DeferredBlock<TerminalBlock<PatternAccessTerminalBlockEntity>> PATTERN_ACCESS_TERMINAL = terminal(AEParts.PATTERN_ACCESS_TERMINAL);

    public static final DeferredBlock<MonitorBlock<StorageMonitorBlockEntity>> STORAGE_MONITOR = monitor(AEParts.STORAGE_MONITOR);
    public static final DeferredBlock<MonitorBlock<ConversionMonitorBlockEntity>> CONVERSION_MONITOR = monitor(AEParts.CONVERSION_MONITOR);
    // spotless:on

    public static final DeferredBlock<TerminalBlock<RequesterTerminalBlockEntity>> REQUESTER_TERMINAL = block(
            "requester_terminal",
            RequesterTerminalBlock::new,
            block -> new IntegrationBlockItem(block, Addons.MEREQUESTER));

    static <P extends AbstractDisplayPart, E extends TerminalBlockEntity> DeferredBlock<TerminalBlock<E>> terminal(
            ItemDefinition<PartItem<P>> equivalentPart) {
        return block(equivalentPart.id().getPath(), () -> new TerminalBlock<>(equivalentPart));
    }

    static <P extends AbstractMonitorPart, E extends StorageMonitorBlockEntity> DeferredBlock<MonitorBlock<E>> monitor(
            ItemDefinition<PartItem<P>> equivalentPart) {
        return block(equivalentPart.id().getPath(), () -> new MonitorBlock<>(equivalentPart));
    }

    static <T extends Block> DeferredBlock<T> block(String id, Supplier<T> supplier) {
        return block(id, supplier, block -> new AEBaseBlockItem(block, new Item.Properties()));
    }

    static <T extends Block> DeferredBlock<T> block(
            String id, Supplier<T> blockSupplier, Function<T, ? extends AEBaseBlockItem> itemFunction) {
        var block = BLOCKS.register(id, blockSupplier);
        ITEMS.register(id, () -> itemFunction.apply(block.get()));
        return block;
    }
}
