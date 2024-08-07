package gripe._90.fulleng.mixin.client;

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
    @Inject(method = "lambda$loadBlockModels$8", at = @At("HEAD"))
    private static void onBeginLoadModel(
            Map.Entry<ResourceLocation, Resource> entry,
            CallbackInfoReturnable<Pair<ResourceLocation, BlockModel>> cir) {
        if (entry.getKey().getNamespace().equals(FullblockEnergistics.MODID)) {
            UnlitQuadHooksAccessor.enableUnlitExtensions().set(true);
        }
    }
}
