package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.Registration;
import com.almostreliable.merequester.terminal.RequesterTerminalMenu;

import net.minecraft.world.inventory.MenuType;

import appeng.core.definitions.ItemDefinition;

public final class RequesterIntegration {
    public static ItemDefinition<?> getRequesterTerminalPart() {
        return Registration.REQUESTER_TERMINAL;
    }

    static MenuType<?> getRequesterTerminalMenu() {
        return RequesterTerminalMenu.TYPE;
    }
}
