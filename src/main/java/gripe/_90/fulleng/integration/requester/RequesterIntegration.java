package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.core.Registration;
import com.almostreliable.merequester.terminal.RequesterTerminalMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;

public final class RequesterIntegration {
    public static final ItemLike REQUESTER_TERMINAL = Registration.REQUESTER_TERMINAL;

    static MenuType<?> getRequesterTerminalMenu() {
        return RequesterTerminalMenu.TYPE;
    }
}
