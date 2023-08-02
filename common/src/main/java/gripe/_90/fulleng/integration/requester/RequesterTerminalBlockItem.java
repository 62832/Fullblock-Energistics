package gripe._90.fulleng.integration.requester;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import appeng.block.AEBaseBlockItem;
import appeng.core.CreativeTab;
import appeng.core.localization.Tooltips;

import gripe._90.fulleng.FullblockEnergistics;

public class RequesterTerminalBlockItem extends AEBaseBlockItem {
    public RequesterTerminalBlockItem(Block id) {
        super(id, new Item.Properties().tab(CreativeTab.INSTANCE));
    }

    @Override
    public void addCheckedInformation(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            tooltip.add(Tooltips.of(RequesterIntegration.NOT_INSTALLED));
        }
    }
}
