package gripe._90.fulleng.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;

import appeng.core.sync.packets.PartLeftClickPacket;

import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;

@Mixin(PartLeftClickPacket.class)
public class PartLeftClickPacketMixin {
    @Shadow
    private BlockHitResult hitResult;

    @Shadow
    private boolean alternateUseMode;

    @Inject(method = "serverPacketData", at = @At("TAIL"))
    private void handleConversionMonitorClick(ServerPlayer player, CallbackInfo ci) {
        if (player.level().getBlockEntity(hitResult.getBlockPos()) instanceof ConversionMonitorBlockEntity monitor) {
            if (hitResult.getDirection().equals(monitor.getFront())) {
                monitor.extractItem(player, alternateUseMode);
            }
        }
    }
}
