package gripe._90.fulleng;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import appeng.api.IAEAddonEntrypoint;
import appeng.core.AppEng;
import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.definition.FullEngBlockEntities;
import gripe._90.fulleng.definition.FullEngBlocks;
import gripe._90.fulleng.integration.requester.RequesterTerminalMenu;
import gripe._90.fulleng.menu.PatternAccessTerminalMenu;
import gripe._90.fulleng.platform.Loader;

@SuppressWarnings("UnstableApiUsage")
public class FullEngFabric implements IAEAddonEntrypoint {
    @Override
    public void onAe2Initialized() {
        FullEngBlocks.getBlocks().forEach(this::registerBlock);
        FullEngBlockEntities.getBlockEntities().forEach(this::registerBlockEntity);

        registerMenus();
        registerPatternStorage();
    }

    private void registerBlock(BlockDefinition<?> block) {
        Registry.register(Registry.BLOCK, block.id(), block.block());
        Registry.register(Registry.ITEM, block.id(), block.asItem());
    }

    private void registerBlockEntity(ResourceLocation id, BlockEntityType<?> blockEntityType) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    private void registerMenus() {
        Registry.register(Registry.MENU, AppEng.makeId("patternaccessterminal_f"),
                PatternAccessTerminalMenu.TYPE_FULLBLOCK);

        if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            Registry.register(Registry.MENU, AppEng.makeId("requester_terminal_f"),
                    RequesterTerminalMenu.TYPE_FULLBLOCK);
        }
    }

    private void registerPatternStorage() {
        ItemStorage.SIDED.registerForBlockEntity((be, context) -> be.getLogic().getBlankPatternInv().toStorage(),
                FullEngBlockEntities.PATTERN_ENCODING_TERMINAL);
    }

    public static class Platform implements gripe._90.fulleng.platform.Platform {
        @Override
        public Loader getLoader() {
            return Loader.FABRIC;
        }

        @Override
        public boolean isRequesterLoaded() {
            return FabricLoader.getInstance().isModLoaded("merequester");
        }
    }
}
