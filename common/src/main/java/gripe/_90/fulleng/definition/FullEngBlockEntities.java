package gripe._90.fulleng.definition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;
import gripe._90.fulleng.block.entity.terminal.CraftingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.block.entity.terminal.StorageTerminalBlockEntity;
import gripe._90.fulleng.integration.requester.RequesterTerminalBlockEntity;

public class FullEngBlockEntities {
    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITIES = new HashMap<>();

    public static Map<ResourceLocation, BlockEntityType<?>> getBlockEntities() {
        return Collections.unmodifiableMap(BLOCK_ENTITIES);
    }

    public static final BlockEntityType<StorageTerminalBlockEntity> TERMINAL = blockEntity(
            "terminal", StorageTerminalBlockEntity.class, StorageTerminalBlockEntity::new, FullEngBlocks.TERMINAL);
    public static final BlockEntityType<CraftingTerminalBlockEntity> CRAFTING_TERMINAL = blockEntity(
            "crafting_terminal",
            CraftingTerminalBlockEntity.class,
            CraftingTerminalBlockEntity::new,
            FullEngBlocks.CRAFTING_TERMINAL);
    public static final BlockEntityType<PatternEncodingTerminalBlockEntity> PATTERN_ENCODING_TERMINAL = blockEntity(
            "pattern_encoding_terminal",
            PatternEncodingTerminalBlockEntity.class,
            PatternEncodingTerminalBlockEntity::new,
            FullEngBlocks.PATTERN_ENCODING_TERMINAL);
    public static final BlockEntityType<PatternAccessTerminalBlockEntity> PATTERN_ACCESS_TERMINAL = blockEntity(
            "pattern_access_terminal",
            PatternAccessTerminalBlockEntity.class,
            PatternAccessTerminalBlockEntity::new,
            FullEngBlocks.PATTERN_ACCESS_TERMINAL);

    public static final BlockEntityType<StorageMonitorBlockEntity> STORAGE_MONITOR = blockEntity(
            "storage_monitor",
            StorageMonitorBlockEntity.class,
            StorageMonitorBlockEntity::new,
            FullEngBlocks.STORAGE_MONITOR);
    public static final BlockEntityType<ConversionMonitorBlockEntity> CONVERSION_MONITOR = blockEntity(
            "conversion_monitor",
            ConversionMonitorBlockEntity.class,
            ConversionMonitorBlockEntity::new,
            FullEngBlocks.CONVERSION_MONITOR);

    public static final BlockEntityType<RequesterTerminalBlockEntity> REQUESTER_TERMINAL = blockEntity(
            "requester_terminal",
            RequesterTerminalBlockEntity.class,
            RequesterTerminalBlockEntity::new,
            FullEngBlocks.REQUESTER_TERMINAL);

    static <T extends AEBaseBlockEntity> BlockEntityType<T> blockEntity(
            String id,
            Class<T> entityClass,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            BlockDefinition<? extends AEBaseEntityBlock<T>> block) {
        var type = BlockEntityType.Builder.of(supplier, block.block()).build(null);
        BLOCK_ENTITIES.put(FullblockEnergistics.makeId(id), type);
        AEBaseBlockEntity.registerBlockEntityItem(type, block.asItem());
        block.block().setBlockEntity(entityClass, type, null, null);
        return type;
    }
}
