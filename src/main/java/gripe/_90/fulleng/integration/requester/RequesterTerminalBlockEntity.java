package gripe._90.fulleng.integration.requester;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;
import gripe._90.fulleng.definition.FullEngBEs;
import gripe._90.fulleng.integration.Addons;

public class RequesterTerminalBlockEntity extends TerminalBlockEntity {
    public RequesterTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBEs.REQUESTER_TERMINAL.get(), pos, blockState);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Addons.MEREQUESTER.isLoaded() ? RequesterTerminalMenu.TYPE_FULLBLOCK : null;
    }
}
