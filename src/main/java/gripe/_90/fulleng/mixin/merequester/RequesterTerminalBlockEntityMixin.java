package gripe._90.fulleng.mixin.merequester;

import com.almostreliable.merequester.terminal.RequesterTerminalHost;
import org.spongepowered.asm.mixin.Mixin;

import gripe._90.fulleng.integration.requester.RequesterTerminalBlockEntity;

@Mixin(RequesterTerminalBlockEntity.class)
public abstract class RequesterTerminalBlockEntityMixin implements RequesterTerminalHost {}
