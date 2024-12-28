package gripe._90.fulleng.client;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import appeng.api.util.AEColor;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.core.network.serverbound.PartLeftClickPacket;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;

@SuppressWarnings("unused")
@Mod(value = FullblockEnergistics.MODID, dist = Dist.CLIENT)
public class FullBlockEnergisticsClient {
    public FullBlockEnergisticsClient(IEventBus eventBus) {
        eventBus.addListener((ModelEvent.RegisterGeometryLoaders event) -> {
            BlockEntityRenderers.register(FullblockEnergistics.STORAGE_MONITOR_BE.get(), MonitorBERenderer::new);
            BlockEntityRenderers.register(FullblockEnergistics.CONVERSION_MONITOR_BE.get(), MonitorBERenderer::new);
        });

        eventBus.addListener((RegisterColorHandlersEvent.Block event) -> {
            for (var block : FullblockEnergistics.BLOCKS.getEntries()) {
                event.register(ColorableBlockEntityBlockColor.INSTANCE, block.get());
            }
        });

        eventBus.addListener((RegisterColorHandlersEvent.Item event) -> {
            for (var block : FullblockEnergistics.BLOCKS.getEntries()) {
                event.register(new StaticItemColor(AEColor.TRANSPARENT), block.get());
            }
        });

        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.LeftClickBlock event) -> {
            var level = event.getLevel();

            if (level.isClientSide()) {
                if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)) {
                    return;
                }

                if (level.getBlockEntity(hitResult.getBlockPos()) instanceof ConversionMonitorBlockEntity monitor
                        && hitResult.getDirection() == monitor.getFront()) {
                    PacketDistributor.sendToServer(
                            new PartLeftClickPacket(hitResult, event.getEntity().isShiftKeyDown()));
                    Objects.requireNonNull(Minecraft.getInstance().gameMode).destroyDelay = 5;
                    event.setCanceled(true);
                }
            }
        });
    }
}
