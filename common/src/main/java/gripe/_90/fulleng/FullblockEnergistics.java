package gripe._90.fulleng;

import java.util.ServiceLoader;

import net.minecraft.resources.ResourceLocation;

public final class FullblockEnergistics {
    public static final String MODID = "fulleng";
    public static final Platform PLATFORM =
            ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    public static ResourceLocation makeId(String path) {
        return new ResourceLocation(MODID, path);
    }

    public interface Platform {
        boolean isRequesterLoaded();
    }
}
