package gripe._90.fulleng.mixin;

import java.util.Map;

import com.mojang.datafixers.util.Pair;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import gripe._90.fulleng.FullblockEnergistics;

@Mixin(ModelManager.class)
public abstract class ModelManagerMixin {
    @Inject(method = "method_45898", at = @At("HEAD"), allow = 1)
    private static void onBeginLoadModel(
            Map.Entry<ResourceLocation, Resource> entry,
            CallbackInfoReturnable<Pair<ResourceLocation, BlockModel>> cir) {
        if (entry.getKey().getNamespace().equals(FullblockEnergistics.MODID)) {
            UnlitQuadHooksAccessor.enableUnlitExtensions().set(true);
        }
    }
}
