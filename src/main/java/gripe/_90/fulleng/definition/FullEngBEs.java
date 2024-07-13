package gripe._90.fulleng.definition;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;
import gripe._90.fulleng.block.entity.terminal.CraftingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.StorageTerminalBlockEntity;
import gripe._90.fulleng.integration.requester.RequesterTerminalBlockEntity;

public class FullEngBEs {
    public static final DeferredRegister<BlockEntityType<?>> DR =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FullblockEnergistics.MODID);

    public static final Supplier<BlockEntityType<StorageTerminalBlockEntity>> TERMINAL =
            be("terminal", StorageTerminalBlockEntity.class, StorageTerminalBlockEntity::new, FullEngBlocks.TERMINAL);
    public static final Supplier<BlockEntityType<CraftingTerminalBlockEntity>> CRAFTING_TERMINAL = be(
            "crafting_terminal",
            CraftingTerminalBlockEntity.class,
            CraftingTerminalBlockEntity::new,
            FullEngBlocks.CRAFTING_TERMINAL);
    public static final Supplier<BlockEntityType<PatternEncodingTerminalBlockEntity>> PATTERN_ENCODING_TERMINAL = be(
            "pattern_encoding_terminal",
            PatternEncodingTerminalBlockEntity.class,
            PatternEncodingTerminalBlockEntity::new,
            FullEngBlocks.PATTERN_ENCODING_TERMINAL);
    public static final Supplier<BlockEntityType<PatternAccessTerminalBlockEntity>> PATTERN_ACCESS_TERMINAL = be(
            "pattern_access_terminal",
            PatternAccessTerminalBlockEntity.class,
            PatternAccessTerminalBlockEntity::new,
            FullEngBlocks.PATTERN_ACCESS_TERMINAL);

    public static final Supplier<BlockEntityType<StorageMonitorBlockEntity>> STORAGE_MONITOR = be(
            "storage_monitor",
            StorageMonitorBlockEntity.class,
            StorageMonitorBlockEntity::new,
            FullEngBlocks.STORAGE_MONITOR);
    public static final Supplier<BlockEntityType<ConversionMonitorBlockEntity>> CONVERSION_MONITOR = be(
            "conversion_monitor",
            ConversionMonitorBlockEntity.class,
            ConversionMonitorBlockEntity::new,
            FullEngBlocks.CONVERSION_MONITOR);

    public static final Supplier<BlockEntityType<RequesterTerminalBlockEntity>> REQUESTER_TERMINAL = be(
            "requester_terminal",
            RequesterTerminalBlockEntity.class,
            RequesterTerminalBlockEntity::new,
            FullEngBlocks.REQUESTER_TERMINAL);

    @SuppressWarnings("DataFlowIssue")
    static <T extends AEBaseBlockEntity> Supplier<BlockEntityType<T>> be(
            String id,
            Class<T> entityClass,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            Supplier<? extends AEBaseEntityBlock<T>> block) {
        return DR.register(id, () -> {
            var type = BlockEntityType.Builder.of(supplier, block.get()).build(null);
            AEBaseBlockEntity.registerBlockEntityItem(type, block.get().asItem());
            block.get().setBlockEntity(entityClass, type, null, null);
            return type;
        });
    }
}
