package gripe._90.fulleng;

import com.almostreliable.merequester.client.RequesterTerminalScreen;
import com.almostreliable.merequester.terminal.RequesterTerminalMenu;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.SecurityPermissions;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;
import appeng.init.client.InitScreens;
import appeng.menu.implementations.MenuTypeBuilder;

import gripe._90.fulleng.block.TerminalBlock;
import gripe._90.fulleng.block.entity.TerminalBlockEntity;

public final class RequesterIntegration {
    public static void init() {
        // load static
    }

    public static void initScreen() {
        InitScreens.<Menu, RequesterTerminalScreen<Menu>>register(Menu.TYPE_FULLBLOCK, RequesterTerminalScreen::new,
                "/screens/requester_terminal.json");
    }

    // spotless:off
    public static final BlockDefinition<TerminalBlock<BlockEntity>> REQUESTER_TERMINAL_BLOCK = FullblockEnergistics.block("ME Requester Terminal", "requester_terminal", Block::new);
    public static final BlockEntityType<BlockEntity> REQUESTER_TERMINAL = FullblockEnergistics.blockEntity("requester_terminal", BlockEntity.class, BlockEntity::new, REQUESTER_TERMINAL_BLOCK);
    // spotless:on

    private static class Block extends TerminalBlock<BlockEntity> {
        public Block() {
            // not used anyway
            super(AEParts.TERMINAL);
        }

        @Override
        public @NotNull String getDescriptionId() {
            return "item.merequester.requester_terminal";
        }
    }

    private static class BlockEntity extends TerminalBlockEntity {
        BlockEntity(BlockPos pos, BlockState blockState) {
            super(REQUESTER_TERMINAL, pos, blockState);
        }

        @Override
        public MenuType<?> getMenuType(Player player) {
            return Menu.TYPE_FULLBLOCK;
        }
    }

    public static class Menu extends RequesterTerminalMenu {
        public static final MenuType<Menu> TYPE_FULLBLOCK = MenuTypeBuilder.create(Menu::new, BlockEntity.class)
                .requirePermission(SecurityPermissions.BUILD)
                .build("requester_terminal_f");

        private Menu(int id, Inventory playerInventory, BlockEntity host) {
            super(TYPE_FULLBLOCK, id, playerInventory, host);
        }
    }
}
