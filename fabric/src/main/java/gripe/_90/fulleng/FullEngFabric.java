package gripe._90.fulleng;

import com.google.common.collect.Sets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import appeng.api.IAEAddonEntrypoint;
import appeng.api.util.AEColor;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.client.render.model.AutoRotatingBakedModel;
import appeng.hooks.ModelsReloadCallback;

public class FullEngFabric implements IAEAddonEntrypoint {
    @Override
    public void onAe2Initialized() {
        if (FabricLoader.getInstance().isModLoaded("merequester")) {
            RequesterIntegration.init();
        }

        FullblockEnergistics.getBlocks().forEach(b -> {
            Registry.register(Registry.BLOCK, b.id(), b.block());
            Registry.register(Registry.ITEM, b.id(), b.asItem());
        });
        FullblockEnergistics.getBlockEntities().forEach((k, v) -> Registry.register(Registry.BLOCK_ENTITY_TYPE, k, v));
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
        }
    }
}
