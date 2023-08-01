package gripe._90.fulleng.integration.requester;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.config.SecurityPermissions;
import appeng.menu.implementations.MenuTypeBuilder;

public class RequesterTerminalMenu extends com.almostreliable.merequester.terminal.RequesterTerminalMenu {
    public static final MenuType<RequesterTerminalMenu> TYPE_FULLBLOCK = MenuTypeBuilder
            .create(RequesterTerminalMenu::new, RequesterTerminalBlockEntity.class)
            .requirePermission(SecurityPermissions.BUILD).build("requester_terminal_f");

    protected RequesterTerminalMenu(int id, Inventory playerInventory, RequesterTerminalBlockEntity host) {
        super(TYPE_FULLBLOCK, id, playerInventory, host);
    }
}
