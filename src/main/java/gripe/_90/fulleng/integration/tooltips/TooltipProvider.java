package gripe._90.fulleng.integration.tooltips;

import net.minecraft.resources.ResourceLocation;

import appeng.api.integrations.igtooltip.ClientRegistration;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class TooltipProvider implements appeng.api.integrations.igtooltip.TooltipProvider {
    @Override
    public void registerClient(ClientRegistration registration) {
        registration.addBlockEntityBody(
                StorageMonitorBlockEntity.class,
                MonitorBlock.class,
                ResourceLocation.fromNamespaceAndPath(FullblockEnergistics.MODID, "storage_monitor"),
                new MonitorDataProvider());
    }
}
