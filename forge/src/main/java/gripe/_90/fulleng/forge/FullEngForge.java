package gripe._90.fulleng.forge;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.block.entity.PatternEncodingTerminalBlockEntity;
import gripe._90.fulleng.integration.RequesterIntegration;

@Mod(FullblockEnergistics.MODID)
public class FullEngForge {
    public FullEngForge() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registry.BLOCK_REGISTRY)) {
                FullblockEnergistics.getBlocks().forEach(b -> {
                    ForgeRegistries.BLOCKS.register(b.id(), b.block());
                    ForgeRegistries.ITEMS.register(b.id(), b.asItem());
                });
            }

            if (event.getRegistryKey().equals(Registry.BLOCK_ENTITY_TYPE_REGISTRY)) {
                FullblockEnergistics.getBlockEntities().forEach(ForgeRegistries.BLOCK_ENTITY_TYPES::register);
            }

            if (event.getRegistryKey().equals(Registry.MENU_REGISTRY)) {
                ForgeRegistries.MENU_TYPES.register("appeng:patternaccessterminal_f",
                        PatternAccessTerminalBlockEntity.Menu.TYPE_FULLBLOCK);

                if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
                    ForgeRegistries.MENU_TYPES.register("appeng:requester_terminal_f",
                            RequesterIntegration.Menu.TYPE_FULLBLOCK);
                }
            }
        });

        // https://www.youtube.com/watch?v=GQPM4_fMIEg&t=44s
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> event) -> {
            if (event.getObject() instanceof PatternEncodingTerminalBlockEntity patternTerm) {
                var capabilityProvider = new ICapabilityProvider() {
                    private final LazyOptional<IItemHandler> patternSlotHandler = LazyOptional
                            .of(() -> patternTerm.getLogic().getBlankPatternInv().toItemHandler());

                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap,
                            @Nullable Direction dir) {
                        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, patternSlotHandler);
                    }

                    private void invalidate() {
                        patternSlotHandler.invalidate();
                    }
                };

                event.addCapability(FullblockEnergistics.makeId("pattern_encoding_terminal"), capabilityProvider);
                event.addListener(capabilityProvider::invalidate);
            }
        });

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> FullEngClient::new);
    }

    public static class Platform implements FullblockEnergistics.Platform {
        @Override
        public boolean isRequesterLoaded() {
            return ModList.get().isLoaded("merequester");
        }
    }
}
