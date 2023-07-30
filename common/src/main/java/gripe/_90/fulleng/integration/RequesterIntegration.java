package gripe._90.fulleng.integration;

import com.almostreliable.merequester.client.RequesterTerminalScreen;
import com.almostreliable.merequester.terminal.RequesterTerminalMenu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.config.SecurityPermissions;
import appeng.init.client.InitScreens;
import appeng.menu.implementations.MenuTypeBuilder;

@SuppressWarnings("RedundantTypeArguments")
public final class RequesterIntegration {
    public static void initScreen() {
        InitScreens.<Menu, RequesterTerminalScreen<Menu>>register(Menu.TYPE_FULLBLOCK, RequesterTerminalScreen::new,
                "/screens/requester_terminal.json");
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
