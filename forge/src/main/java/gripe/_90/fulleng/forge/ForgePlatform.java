package gripe._90.fulleng.forge;

import net.minecraftforge.fml.ModList;

import gripe._90.fulleng.FullblockEnergistics;

public class ForgePlatform implements FullblockEnergistics.Platform {
    @Override
    public boolean isRequesterLoaded() {
        return ModList.get().isLoaded("merequester");
    }
}
