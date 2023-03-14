package gripe._90.fulleng;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;

import appeng.api.IAEAddonEntrypoint;
import appeng.core.AppEng;

import gripe._90.fulleng.block.entity.PatternAccessTerminalBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class FullEngFabric implements IAEAddonEntrypoint {
    static final boolean REQUESTER_PRESENT = FabricLoader.getInstance().isModLoaded("merequester");

    @Override
    public void onAe2Initialized() {
        if (REQUESTER_PRESENT) {
            RequesterIntegration.init();
        }

        FullblockEnergistics.getBlocks().forEach(b -> {
            Registry.register(Registry.BLOCK, b.id(), b.block());
            Registry.register(Registry.ITEM, b.id(), b.asItem());
        });
        FullblockEnergistics.getBlockEntities().forEach((k, v) -> Registry.register(Registry.BLOCK_ENTITY_TYPE, k, v));

        Registry.register(Registry.MENU, AppEng.makeId("patternaccessterminal_f"),
                PatternAccessTerminalBlockEntity.Menu.TYPE_FULLBLOCK);

        if (REQUESTER_PRESENT) {
            Registry.register(Registry.MENU, AppEng.makeId("requester_terminal_f"),
                    RequesterIntegration.Menu.TYPE_FULLBLOCK);
        }

        ItemStorage.SIDED.registerForBlockEntity((be, context) -> be.getLogic().getBlankPatternInv().toStorage(),
                FullblockEnergistics.PATTERN_ENCODING_TERMINAL);
    }
}
