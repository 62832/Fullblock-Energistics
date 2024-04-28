package gripe._90.fulleng.integration.requester;

import org.jetbrains.annotations.NotNull;

import appeng.core.definitions.AEParts;

import gripe._90.fulleng.block.TerminalBlock;

public class RequesterTerminalBlock extends TerminalBlock<RequesterTerminalBlockEntity> {
    public RequesterTerminalBlock() {
        super(AEParts.TERMINAL);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "item.merequester.requester_terminal";
    }
}
