package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.core.Registration;
import com.almostreliable.merequester.terminal.RequesterTerminalMenu;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredItem;

public final class RequesterIntegration {
    public static DeferredItem<?> getRequesterTerminalPart() {
        return Registration.REQUESTER_TERMINAL;
    }

    static MenuType<?> getRequesterTerminalMenu() {
        return RequesterTerminalMenu.TYPE;
    }
}
