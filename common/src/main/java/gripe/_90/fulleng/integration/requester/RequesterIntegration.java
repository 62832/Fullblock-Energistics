package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.client.RequesterTerminalScreen;

import appeng.init.client.InitScreens;

@SuppressWarnings("RedundantTypeArguments")
public final class RequesterIntegration {
    public static void initScreen() {
        InitScreens.<RequesterTerminalMenu, RequesterTerminalScreen<RequesterTerminalMenu>>register(
                RequesterTerminalMenu.TYPE_FULLBLOCK, RequesterTerminalScreen::new,
                "/screens/requester_terminal.json");
    }
}
