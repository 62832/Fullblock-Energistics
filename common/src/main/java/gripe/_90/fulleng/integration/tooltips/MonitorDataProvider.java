package gripe._90.fulleng.integration.tooltips;

import appeng.api.integrations.igtooltip.TooltipBuilder;
import appeng.api.integrations.igtooltip.TooltipContext;
import appeng.api.integrations.igtooltip.providers.BodyProvider;
import appeng.core.localization.InGameTooltip;

import gripe._90.fulleng.block.entity.monitor.MonitorBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public final class MonitorDataProvider implements BodyProvider<MonitorBlockEntity> {
    @Override
    public void buildTooltip(MonitorBlockEntity monitor, TooltipContext context, TooltipBuilder tooltip) {
        var displayed = monitor.getDisplayed();
        var isLocked = monitor.isLocked();

        if (displayed != null) {
            tooltip.addLine(InGameTooltip.Showing.text().append(": ")
                    .append(displayed.getDisplayName()));
        }

        tooltip.addLine(isLocked ? InGameTooltip.Locked.text() : InGameTooltip.Unlocked.text());
    }
}
