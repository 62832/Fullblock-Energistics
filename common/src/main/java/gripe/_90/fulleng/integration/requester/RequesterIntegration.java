package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.client.RequesterTerminalScreen;

import net.minecraft.network.chat.Component;

import appeng.init.client.InitScreens;

import gripe._90.fulleng.FullblockEnergistics;

@SuppressWarnings("RedundantTypeArguments")
public final class RequesterIntegration {
    public static final Component NOT_INSTALLED =
            Component.translatable("gui." + FullblockEnergistics.MODID + ".RequesterNotInstalled");

    public static void initScreen() {
        InitScreens.<RequesterTerminalMenu, RequesterTerminalScreen<RequesterTerminalMenu>>register(
                RequesterTerminalMenu.TYPE_FULLBLOCK, RequesterTerminalScreen::new, "/screens/requester_terminal.json");
    }
}
