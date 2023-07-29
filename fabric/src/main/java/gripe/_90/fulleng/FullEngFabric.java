package gripe._90.fulleng;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;

import appeng.api.IAEAddonEntrypoint;
import appeng.core.AppEng;

import gripe._90.fulleng.block.entity.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.integration.RequesterIntegration;

@SuppressWarnings("UnstableApiUsage")
public class FullEngFabric implements IAEAddonEntrypoint {
    @Override
    public void onAe2Initialized() {
        FullblockEnergistics.getBlocks().forEach(b -> {
            Registry.register(Registry.BLOCK, b.id(), b.block());
            Registry.register(Registry.ITEM, b.id(), b.asItem());
        });
        FullblockEnergistics.getBlockEntities().forEach((k, v) -> Registry.register(Registry.BLOCK_ENTITY_TYPE, k, v));

        Registry.register(Registry.MENU, AppEng.makeId("patternaccessterminal_f"),
                PatternAccessTerminalBlockEntity.Menu.TYPE_FULLBLOCK);

        if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            Registry.register(Registry.MENU, AppEng.makeId("requester_terminal_f"),
                    RequesterIntegration.Menu.TYPE_FULLBLOCK);
        }

        ItemStorage.SIDED.registerForBlockEntity((be, context) -> be.getLogic().getBlankPatternInv().toStorage(),
                FullblockEnergistics.PATTERN_ENCODING_TERMINAL);
    }

    public static class Platform implements FullblockEnergistics.Platform {
        @Override
        public boolean isRequesterLoaded() {
            return FabricLoader.getInstance().isModLoaded("merequester");
        }
    }
}
