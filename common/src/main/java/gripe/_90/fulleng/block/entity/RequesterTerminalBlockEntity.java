package gripe._90.fulleng.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.menu.me.common.MEStorageMenu;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.RequesterIntegration;

public class RequesterTerminalBlockEntity extends TerminalBlockEntity {
    public RequesterTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.REQUESTER_TERMINAL, pos, blockState);
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return FullblockEnergistics.PLATFORM.isRequesterLoaded() ? RequesterIntegration.Menu.TYPE_FULLBLOCK
                : MEStorageMenu.TYPE;
    }
}
