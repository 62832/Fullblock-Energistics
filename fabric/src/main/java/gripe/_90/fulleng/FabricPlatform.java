package gripe._90.fulleng;

import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements FullblockEnergistics.Platform {
    @Override
    public boolean isRequesterLoaded() {
        return FabricLoader.getInstance().isModLoaded("merequester");
    }
}
