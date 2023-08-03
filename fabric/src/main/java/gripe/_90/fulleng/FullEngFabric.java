package gripe._90.fulleng;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import appeng.api.IAEAddonEntrypoint;
import appeng.api.ids.AECreativeTabIds;
import appeng.core.AppEng;
import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.integration.requester.RequesterTerminalMenu;
import gripe._90.fulleng.menu.PatternAccessTerminalMenu;

@SuppressWarnings("UnstableApiUsage")
public class FullEngFabric implements IAEAddonEntrypoint {
    @Override
    public void onAe2Initialized() {
        FullblockEnergistics.getBlocks().forEach(this::registerBlock);
        FullblockEnergistics.getBlockEntities().forEach(this::registerBlockEntity);

        registerMenus();
        registerPatternStorage();
    }

    private void registerBlock(BlockDefinition<?> block) {
        Registry.register(BuiltInRegistries.BLOCK, block.id(), block.block());
        Registry.register(BuiltInRegistries.ITEM, block.id(), block.asItem());

        ItemGroupEvents.modifyEntriesEvent(AECreativeTabIds.MAIN).register(content -> content.accept(block));
    }

    private void registerBlockEntity(ResourceLocation id, BlockEntityType<?> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    private void registerMenus() {
        Registry.register(
                BuiltInRegistries.MENU,
                AppEng.makeId("patternaccessterminal_f"),
                PatternAccessTerminalMenu.TYPE_FULLBLOCK);

        if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            Registry.register(
                    BuiltInRegistries.MENU,
                    AppEng.makeId("requester_terminal_f"),
                    RequesterTerminalMenu.TYPE_FULLBLOCK);
        }
    }

    private void registerPatternStorage() {
        ItemStorage.SIDED.registerForBlockEntity(
                (be, context) -> be.getLogic().getBlankPatternInv().toStorage(),
                FullblockEnergistics.PATTERN_ENCODING_TERMINAL);
    }

    public static class Platform implements FullblockEnergistics.Platform {
        @Override
        public boolean isRequesterLoaded() {
            return FabricLoader.getInstance().isModLoaded("merequester");
        }
    }
}
