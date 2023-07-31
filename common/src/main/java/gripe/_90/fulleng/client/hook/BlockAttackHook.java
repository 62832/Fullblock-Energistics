package gripe._90.fulleng.client.hook;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;

public final class BlockAttackHook {
    private BlockAttackHook() {
    }

    public static InteractionResult onBlockAttackedOnClient(Player player, Level level) {
        if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)) {
            return InteractionResult.PASS;
        }

        var pos = hitResult.getBlockPos();

        if (level.getBlockState(pos).getBlock() instanceof MonitorBlock<?> monitor
                && monitor.getBlockEntity(level, pos) instanceof ConversionMonitorBlockEntity) {
            monitor.onClicked(level, pos, player);
            Objects.requireNonNull(Minecraft.getInstance().gameMode).destroyDelay = 5;
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }
}
