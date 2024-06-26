package gripe._90.fulleng.integration.requester;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.util.IConfigManagerBuilder;
import appeng.menu.me.common.MEStorageMenu;

import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;
import gripe._90.fulleng.definition.FullEngBlockEntities;
import gripe._90.fulleng.integration.Addons;

public class RequesterTerminalBlockEntity extends TerminalBlockEntity {
    public RequesterTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBlockEntities.REQUESTER_TERMINAL, pos, blockState);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Addons.REQUESTER.isLoaded() ? RequesterTerminalMenu.TYPE_FULLBLOCK : MEStorageMenu.TYPE;
    }

    @Override
    protected void registerSettings(IConfigManagerBuilder builder) {}
}
