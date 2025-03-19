package gripe._90.fulleng.integration.appliede;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.terminal.StorageTerminalBlockEntity;
import gripe._90.fulleng.integration.Addons;

public class TransmutationTerminalBlockEntity extends StorageTerminalBlockEntity {
    public TransmutationTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(FullblockEnergistics.TRANSMUTATION_TERMINAL_BE.get(), pos, state);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Addons.APPLIEDE.isLoaded() ? AppliedEIntegration.TRANSMUTATION_TERMINAL_MENU : super.getMenuType();
    }
}
