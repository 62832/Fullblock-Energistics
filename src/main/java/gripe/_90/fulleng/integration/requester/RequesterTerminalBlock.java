package gripe._90.fulleng.integration.requester;

import org.jetbrains.annotations.NotNull;

import appeng.core.definitions.AEParts;

import gripe._90.fulleng.block.TerminalBlock;
import gripe._90.fulleng.integration.Addons;

public class RequesterTerminalBlock extends TerminalBlock<RequesterTerminalBlockEntity> {
    public RequesterTerminalBlock() {
        super(Addons.MEREQUESTER.isLoaded() ? RequesterIntegration.getRequesterTerminalPart() : AEParts.TERMINAL);
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        return "item." + Addons.MEREQUESTER.getModId() + ".requester_terminal";
    }
}
