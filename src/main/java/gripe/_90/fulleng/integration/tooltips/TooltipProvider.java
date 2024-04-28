package gripe._90.fulleng.integration.tooltips;

import appeng.api.integrations.igtooltip.ClientRegistration;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class TooltipProvider implements appeng.api.integrations.igtooltip.TooltipProvider {
    public static final String STORAGE_MONITOR = "storage_monitor";

    @Override
    public void registerClient(ClientRegistration registration) {
        registration.addBlockEntityBody(
                StorageMonitorBlockEntity.class,
                MonitorBlock.class,
                FullblockEnergistics.makeId(STORAGE_MONITOR),
                new MonitorDataProvider());
    }
}
