package gripe._90.fulleng.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import appeng.hooks.UnlitQuadHooks;

@Mixin(UnlitQuadHooks.class)
public interface UnlitQuadHooksAccessor {
    @Accessor(value = "ENABLE_UNLIT_EXTENSIONS")
    static ThreadLocal<Boolean> enableUnlitExtensions() {
        throw new AssertionError();
    }
}
