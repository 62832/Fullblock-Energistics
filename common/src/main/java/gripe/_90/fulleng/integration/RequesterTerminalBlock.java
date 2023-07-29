package gripe._90.fulleng.integration;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import appeng.block.AEBaseBlockItem;
import appeng.core.CreativeTab;
import appeng.core.definitions.AEParts;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.TerminalBlock;
import gripe._90.fulleng.block.entity.RequesterTerminalBlockEntity;

public class RequesterTerminalBlock extends TerminalBlock<RequesterTerminalBlockEntity> {
    public RequesterTerminalBlock() {
        super(AEParts.TERMINAL);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "item.merequester.requester_terminal";
    }

    public static class BlockItem extends AEBaseBlockItem {
        public BlockItem(Block id) {
            super(id, new Item.Properties().tab(CreativeTab.INSTANCE));
        }

        @Override
        public void addCheckedInformation(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
            if (!FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
                tooltip.add(Component.literal("ME Requester not installed."));
            }
        }
    }
}
