package gripe._90.fulleng.integration.requester;

import com.almostreliable.merequester.client.RequesterTerminalScreen;

import net.minecraft.network.chat.Component;

import appeng.init.client.InitScreens;

import gripe._90.fulleng.FullblockEnergistics;

public final class RequesterIntegration {
    public static final Component NOT_INSTALLED =
            Component.translatable("gui." + FullblockEnergistics.MODID + ".RequesterNotInstalled");

    public static void initScreen() {
        InitScreens.register(
                RequesterTerminalMenu.TYPE_FULLBLOCK,
                RequesterTerminalScreen<RequesterTerminalMenu>::new,
                "/screens/requester_terminal.json");
    }
}
