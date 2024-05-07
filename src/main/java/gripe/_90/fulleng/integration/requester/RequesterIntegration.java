package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.Registration;
import com.almostreliable.merequester.client.RequesterTerminalScreen;

import appeng.core.definitions.ItemDefinition;
import appeng.init.client.InitScreens;

public final class RequesterIntegration {
    static ItemDefinition<?> getRequesterTerminalPart() {
        return Registration.REQUESTER_TERMINAL;
    }

    public static void initScreen() {
        InitScreens.register(
                RequesterTerminalMenu.TYPE_FULLBLOCK,
                RequesterTerminalScreen<RequesterTerminalMenu>::new,
                "/screens/requester_terminal.json");
    }
}
