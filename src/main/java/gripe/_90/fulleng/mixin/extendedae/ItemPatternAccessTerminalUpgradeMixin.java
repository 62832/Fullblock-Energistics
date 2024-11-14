package gripe._90.fulleng.mixin.extendedae;

import com.glodblock.github.extendedae.common.items.ItemPatternAccessTerminalUpgrade;
import com.glodblock.github.extendedae.common.items.ItemUpgrade;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.terminal.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.integration.extendedae.ExtendedPatternAccessTerminalBlockEntity;

@Mixin(ItemPatternAccessTerminalUpgrade.class)
public abstract class ItemPatternAccessTerminalUpgradeMixin extends ItemUpgrade {
    public ItemPatternAccessTerminalUpgradeMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addFullblock(CallbackInfo ci) {
        addTile(
                PatternAccessTerminalBlockEntity.class,
                FullblockEnergistics.EXTENDED_PATTERN_ACCESS_TERMINAL.get(),
                ExtendedPatternAccessTerminalBlockEntity.class);
    }
}
