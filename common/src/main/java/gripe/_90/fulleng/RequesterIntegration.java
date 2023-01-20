package gripe._90.fulleng;

import com.almostreliable.merequester.terminal.RequesterTerminalMenu;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.block.TerminalBlock;
import gripe._90.fulleng.block.entity.TerminalBlockEntity;

public final class RequesterIntegration {
    public static void init() {
        // load static
    }

    // spotless:off
    public static final BlockDefinition<TerminalBlock<RequesterTerminalBlockEntity>> REQUESTER_TERMINAL_BLOCK = FullblockEnergistics.block("ME Requester Terminal", "requester_terminal", RequesterTerminalBlock::new);
    public static final BlockEntityType<RequesterTerminalBlockEntity> REQUESTER_TERMINAL = FullblockEnergistics.blockEntity("requester_terminal", RequesterTerminalBlockEntity.class, RequesterTerminalBlockEntity::new, REQUESTER_TERMINAL_BLOCK);
    // spotless:on

    private static class RequesterTerminalBlock extends TerminalBlock<RequesterTerminalBlockEntity> {
        public RequesterTerminalBlock() {
            // not used anyway
            super(AEParts.TERMINAL);
        }

        @Override
        public @NotNull String getDescriptionId() {
            return "item.merequester.requester_terminal";
        }
    }

    private static class RequesterTerminalBlockEntity extends TerminalBlockEntity {
        RequesterTerminalBlockEntity(BlockPos pos, BlockState blockState) {
            super(REQUESTER_TERMINAL, pos, blockState);
        }

        @Override
        public MenuType<?> getMenuType(Player player) {
            return RequesterTerminalMenu.TYPE;
        }
    }
}
