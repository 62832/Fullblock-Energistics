package gripe._90.fulleng.mixin.extendedae;

import com.glodblock.github.glodium.util.GlodUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.entity.BlockEntityType;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.integration.extendedae.ExtendedPatternAccessTerminalBlockEntity;

@Mixin(GlodUtil.class)
public abstract class GlodUtilMixin {
    @Inject(
            method = "getTileType(Ljava/lang/Class;)Lnet/minecraft/world/level/block/entity/BlockEntityType;",
            at = @At("HEAD"),
            cancellable = true)
    private static void jesusWept(Class<?> clazz, CallbackInfoReturnable<BlockEntityType<?>> cir) {
        if (clazz.equals(ExtendedPatternAccessTerminalBlockEntity.class)) {
            cir.setReturnValue(FullblockEnergistics.EXTENDED_PATTERN_ACCESS_TERMINAL_BE.get());
        }
    }
}
