package gripe._90.fulleng.integration.tooltips;

import appeng.api.integrations.igtooltip.BaseClassRegistration;
import appeng.api.integrations.igtooltip.ClientRegistration;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.block.entity.monitor.MonitorBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class TooltipProvider implements appeng.api.integrations.igtooltip.TooltipProvider {
    public static final String MONITOR = "storage_monitor";

    @Override
    public void registerClient(ClientRegistration registration) {
        registration.addBlockEntityBody(MonitorBlockEntity.class, MonitorBlock.class,
                FullblockEnergistics.makeId(MONITOR), new MonitorDataProvider());
    }

    @Override
    public void registerBlockEntityBaseClasses(BaseClassRegistration registration) {
        registration.addBaseBlockEntity(MonitorBlockEntity.class, MonitorBlock.class);
    }
}
