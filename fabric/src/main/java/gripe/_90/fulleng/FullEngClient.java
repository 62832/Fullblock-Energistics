package gripe._90.fulleng;

import com.google.common.collect.Sets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;

import appeng.api.IAEAddonEntrypoint;
import appeng.api.util.AEColor;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.client.render.model.AutoRotatingBakedModel;
import appeng.hooks.ModelsReloadCallback;
import appeng.init.client.InitScreens;

import gripe._90.fulleng.block.entity.terminal.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.client.renderer.MonitorBlockEntityRenderer;
import gripe._90.fulleng.integration.RequesterIntegration;

@SuppressWarnings("RedundantTypeArguments")
@Environment(EnvType.CLIENT)
public class FullEngClient implements IAEAddonEntrypoint {
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

        BlockEntityRenderers.register(FullblockEnergistics.STORAGE_MONITOR, MonitorBlockEntityRenderer::new);
        BlockEntityRenderers.register(FullblockEnergistics.CONVERSION_MONITOR, MonitorBlockEntityRenderer::new);

        InitScreens.<PatternAccessTerminalBlockEntity.Menu, PatternAccessTermScreen<PatternAccessTerminalBlockEntity.Menu>>register(
                PatternAccessTerminalBlockEntity.Menu.TYPE_FULLBLOCK, PatternAccessTermScreen::new,
                "/screens/pattern_access_terminal.json");

        if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            RequesterIntegration.initScreen();
        }
    }
}
