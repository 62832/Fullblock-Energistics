package gripe._90.fulleng;

import com.google.common.collect.Sets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import appeng.api.IAEAddonEntrypoint;
import appeng.api.util.AEColor;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.client.render.model.AutoRotatingBakedModel;
import appeng.core.AppEng;
import appeng.hooks.ModelsReloadCallback;
import appeng.init.client.InitScreens;

import gripe._90.fulleng.block.entity.PatternAccessTerminalBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class FullEngFabric implements IAEAddonEntrypoint {
    private static final boolean REQUESTER_PRESENT = FabricLoader.getInstance().isModLoaded("merequester");

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

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    public static class Client implements IAEAddonEntrypoint {
        @Override
        public void onAe2Initialized() {
            FullblockEnergistics.getBlocks().forEach(b -> {
                ColorProviderRegistry.BLOCK.register(new ColorableBlockEntityBlockColor(), b.block());
                ColorProviderRegistry.ITEM.register(new StaticItemColor(AEColor.TRANSPARENT), b.block());
                BlockRenderLayerMap.INSTANCE.putBlock(b.block(), RenderType.cutout());
            });

            ModelsReloadCallback.EVENT.register(modelRegistry -> {
                for (ResourceLocation location : Sets.newHashSet(modelRegistry.keySet())) {
                    if (!location.getNamespace().equals(FullblockEnergistics.MODID)) {
                        continue;
                    }

                    modelRegistry.put(location, new AutoRotatingBakedModel(modelRegistry.get(location)));
                }
            });

            InitScreens.<PatternAccessTerminalBlockEntity.Menu, PatternAccessTermScreen<PatternAccessTerminalBlockEntity.Menu>>register(
                    PatternAccessTerminalBlockEntity.Menu.TYPE_FULLBLOCK, PatternAccessTermScreen::new,
                    "/screens/pattern_access_terminal.json");

            if (REQUESTER_PRESENT) {
                RequesterIntegration.initScreen();
            }
        }
    }
}
