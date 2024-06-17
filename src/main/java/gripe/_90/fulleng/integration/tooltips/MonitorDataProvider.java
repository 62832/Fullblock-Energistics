package gripe._90.fulleng.integration.tooltips;

import appeng.api.integrations.igtooltip.TooltipBuilder;
import appeng.api.integrations.igtooltip.TooltipContext;
import appeng.api.integrations.igtooltip.providers.BodyProvider;
import appeng.core.localization.InGameTooltip;

import gripe._90.fulleng.block.entity.monitor.StorageMonitorBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public final class MonitorDataProvider implements BodyProvider<StorageMonitorBlockEntity> {
    @Override
    public void buildTooltip(StorageMonitorBlockEntity monitor, TooltipContext context, TooltipBuilder tooltip) {
        var displayed = monitor.getDisplayed();

        if (displayed != null) {
            tooltip.addLine(InGameTooltip.Showing.text().append(": ").append(displayed.getDisplayName()));
        }

        tooltip.addLine(monitor.isLocked() ? InGameTooltip.Locked.text() : InGameTooltip.Unlocked.text());
    }
}
