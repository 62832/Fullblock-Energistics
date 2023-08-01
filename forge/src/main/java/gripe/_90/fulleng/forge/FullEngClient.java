package gripe._90.fulleng.forge;

import java.util.Objects;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import appeng.api.util.AEColor;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.client.render.model.AutoRotatingBakedModel;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PartLeftClickPacket;
import appeng.init.client.InitScreens;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.client.renderer.MonitorBlockEntityRenderer;
import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.menu.PatternAccessTerminalMenu;

@SuppressWarnings({ "RedundantTypeArguments", "removal" })
public class FullEngClient {
    public FullEngClient() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener((RegisterColorHandlersEvent.Block event) -> FullblockEnergistics.getBlocks()
                .forEach(b -> event.register(new ColorableBlockEntityBlockColor(), b.block())));
        bus.addListener((RegisterColorHandlersEvent.Item event) -> FullblockEnergistics.getBlocks()
                .forEach(b -> event.register(new StaticItemColor(AEColor.TRANSPARENT), b.block())));

        bus.addListener((FMLClientSetupEvent event) -> {
            FullblockEnergistics.getBlocks()
                    .forEach(b -> ItemBlockRenderTypes.setRenderLayer(b.block(), RenderType.cutout()));
            InitScreens.<PatternAccessTerminalMenu, PatternAccessTermScreen<PatternAccessTerminalMenu>>register(
                    PatternAccessTerminalMenu.TYPE_FULLBLOCK, PatternAccessTermScreen::new,
                    "/screens/pattern_access_terminal.json");

            if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
                RequesterIntegration.initScreen();
            }
        });

        bus.addListener((ModelEvent.RegisterGeometryLoaders event) -> {
            BlockEntityRenderers.register(FullblockEnergistics.STORAGE_MONITOR, MonitorBlockEntityRenderer::new);
            BlockEntityRenderers.register(FullblockEnergistics.CONVERSION_MONITOR, MonitorBlockEntityRenderer::new);
        });

        bus.addListener((ModelEvent.BakingCompleted event) -> {
            var modelRegistry = event.getModels();
            for (ResourceLocation location : Sets.newHashSet(modelRegistry.keySet())) {
                if (!location.getNamespace().equals(FullblockEnergistics.MODID)) {
                    continue;
                }

                modelRegistry.put(location, new AutoRotatingBakedModel(modelRegistry.get(location)));
            }
        });

        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.LeftClickBlock event) -> {
            var level = event.getLevel();

            if (level.isClientSide()) {
                if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)) {
                    return;
                }

                if (level.getBlockEntity(hitResult.getBlockPos()) instanceof ConversionMonitorBlockEntity) {
                    NetworkHandler.instance().sendToServer(new PartLeftClickPacket(hitResult,
                            InteractionUtil.isInAlternateUseMode(event.getEntity())));
                    Objects.requireNonNull(Minecraft.getInstance().gameMode).destroyDelay = 5;
                    event.setCanceled(true);
                }
            }
        });
    }
}
