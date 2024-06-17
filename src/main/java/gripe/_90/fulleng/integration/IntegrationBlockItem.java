package gripe._90.fulleng.integration;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import appeng.block.AEBaseBlockItem;

public class IntegrationBlockItem extends AEBaseBlockItem {
    private final Addons addon;

    public IntegrationBlockItem(Block id, Addons addon) {
        super(id, new Properties());
        this.addon = addon;
    }

    @Override
    public void addCheckedInformation(
            ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag advTooltips) {
        if (!addon.isLoaded()) {
            lines.add(addon.getNotInstalledTooltip());
        }
    }
}
