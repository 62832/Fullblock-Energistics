package gripe._90.fulleng.forge;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
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
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PartLeftClickPacket;
import appeng.init.client.InitScreens;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.client.MonitorBlockEntityRenderer;
import gripe._90.fulleng.definition.FullEngBlockEntities;
import gripe._90.fulleng.definition.FullEngBlocks;
import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.menu.PatternAccessTerminalMenu;

@SuppressWarnings({"RedundantTypeArguments", "deprecation"})
public class FullEngClient {
    public FullEngClient() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::initScreens);
        bus.addListener(this::initBlockEntityRenders);

        bus.addListener(this::registerColourProviders);
        bus.addListener(this::setRenderLayers);

        MinecraftForge.EVENT_BUS.addListener(this::addConversionMonitorHook);
    }

    private void initScreens(FMLClientSetupEvent event) {
        InitScreens.<PatternAccessTerminalMenu, PatternAccessTermScreen<PatternAccessTerminalMenu>>register(
                PatternAccessTerminalMenu.TYPE_FULLBLOCK,
                PatternAccessTermScreen::new,
                "/screens/pattern_access_terminal.json");

        if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            RequesterIntegration.initScreen();
        }
    }

    private void initBlockEntityRenders(ModelEvent.RegisterGeometryLoaders event) {
        BlockEntityRenderers.register(FullEngBlockEntities.STORAGE_MONITOR, MonitorBlockEntityRenderer::new);
        BlockEntityRenderers.register(FullEngBlockEntities.CONVERSION_MONITOR, MonitorBlockEntityRenderer::new);
    }

    private void registerColourProviders(RegisterColorHandlersEvent event) {
        for (var block : FullEngBlocks.getBlocks()) {
            if (event instanceof RegisterColorHandlersEvent.Block blockEvent) {
                blockEvent.register(new ColorableBlockEntityBlockColor(), block.block());
            }

            if (event instanceof RegisterColorHandlersEvent.Item itemEvent) {
                itemEvent.register(new StaticItemColor(AEColor.TRANSPARENT), block.block());
            }
        }
    }

    private void setRenderLayers(FMLClientSetupEvent event) {
        for (var block : FullEngBlocks.getBlocks()) {
            ItemBlockRenderTypes.setRenderLayer(block.block(), RenderType.cutout());
        }
    }

    private void addConversionMonitorHook(PlayerInteractEvent.LeftClickBlock event) {
        var level = event.getLevel();

        if (level.isClientSide()) {
            if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)) {
                return;
            }

            if (level.getBlockEntity(hitResult.getBlockPos()) instanceof ConversionMonitorBlockEntity) {
                NetworkHandler.instance()
                        .sendToServer(new PartLeftClickPacket(
                                hitResult, InteractionUtil.isInAlternateUseMode(event.getEntity())));
                Objects.requireNonNull(Minecraft.getInstance().gameMode).destroyDelay = 5;
                event.setCanceled(true);
            }
        }
    }
}
