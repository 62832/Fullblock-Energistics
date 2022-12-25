package gripe._90.fulleng.forge;

import com.google.common.collect.Sets;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import appeng.api.util.AEColor;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.client.render.model.AutoRotatingBakedModel;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.RequesterIntegration;

@Mod(FullblockEnergistics.MODID)
public class FullEngForge {

    public FullEngForge() {
        if (ModList.get().isLoaded("merequester")) {
            RequesterIntegration.init();
        }

        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registry.BLOCK_REGISTRY)) {
                FullblockEnergistics.getBlocks().forEach(b -> {
                    ForgeRegistries.BLOCKS.register(b.id(), b.block());
                    ForgeRegistries.ITEMS.register(b.id(), b.asItem());
                });
            }
            if (event.getRegistryKey().equals(Registry.BLOCK_ENTITY_TYPE_REGISTRY)) {
                FullblockEnergistics.getBlockEntities().forEach(ForgeRegistries.BLOCK_ENTITY_TYPES::register);
            }
        });

        bus.addListener(FullEngDataGenerators::onGatherData);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Client::new);
    }

    private static class Client {
        private Client() {
            var bus = FMLJavaModLoadingContext.get().getModEventBus();

            bus.addListener((RegisterColorHandlersEvent.Block event) -> FullblockEnergistics.getBlocks()
                    .forEach(b -> event.register(new ColorableBlockEntityBlockColor(), b.block())));
            bus.addListener((RegisterColorHandlersEvent.Item event) -> FullblockEnergistics.getBlocks()
                    .forEach(b -> event.register(new StaticItemColor(AEColor.TRANSPARENT), b.block())));
            bus.addListener((FMLClientSetupEvent event) -> FullblockEnergistics.getBlocks()
                    .forEach(b -> ItemBlockRenderTypes.setRenderLayer(b.block(), RenderType.cutout())));

            bus.addListener((ModelEvent.BakingCompleted event) -> {
                var modelRegistry = event.getModels();
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
