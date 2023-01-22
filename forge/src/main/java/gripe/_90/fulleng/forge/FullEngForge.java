package gripe._90.fulleng.forge;

import com.google.common.collect.Sets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import appeng.api.util.AEColor;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.client.render.model.AutoRotatingBakedModel;
import appeng.init.client.InitScreens;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.RequesterIntegration;
import gripe._90.fulleng.block.entity.PatternAccessTerminalBlockEntity;
import gripe._90.fulleng.block.entity.PatternEncodingTerminalBlockEntity;

@Mod(FullblockEnergistics.MODID)
public class FullEngForge {
    private static final boolean REQUESTER_PRESENT = ModList.get().isLoaded("merequester");

    public FullEngForge() {
        if (REQUESTER_PRESENT) {
            RequesterIntegration.init();
        }

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

                if (REQUESTER_PRESENT) {
                    ForgeRegistries.MENU_TYPES.register("appeng:requester_terminal_f",
                            RequesterIntegration.Menu.TYPE_FULLBLOCK);
                }
            }
        });

        // https://www.youtube.com/watch?v=GQPM4_fMIEg&t=44s
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> event) -> {
            if (event.getObject()instanceof PatternEncodingTerminalBlockEntity patternTerm) {
                var capabilityProvider = new ICapabilityProvider() {
                    private LazyOptional<IItemHandler> patternSlotHandler;

                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap,
                            @Nullable Direction dir) {
                        patternSlotHandler = LazyOptional
                                .of(() -> patternTerm.getLogic().getBlankPatternInv().toItemHandler());
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

        var clientSetup = new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                bus.addListener((RegisterColorHandlersEvent.Block event) -> FullblockEnergistics.getBlocks()
                        .forEach(b -> event.register(new ColorableBlockEntityBlockColor(), b.block())));
                bus.addListener((RegisterColorHandlersEvent.Item event) -> FullblockEnergistics.getBlocks()
                        .forEach(b -> event.register(new StaticItemColor(AEColor.TRANSPARENT), b.block())));

                bus.addListener((FMLClientSetupEvent event) -> {
                    FullblockEnergistics.getBlocks()
                            .forEach(b -> ItemBlockRenderTypes.setRenderLayer(b.block(), RenderType.cutout()));
                    InitScreens.<PatternAccessTerminalBlockEntity.Menu, PatternAccessTermScreen<PatternAccessTerminalBlockEntity.Menu>>register(
                            PatternAccessTerminalBlockEntity.Menu.TYPE_FULLBLOCK, PatternAccessTermScreen::new,
                            "/screens/pattern_access_terminal.json");

                    if (REQUESTER_PRESENT) {
                        RequesterIntegration.initScreen();
                    }
                });

                bus.addListener((ModelEvent.BakingCompleted event) -> {
                    var modelRegistry = event.getModels();
                    for (ResourceLocation location : Sets.newHashSet(modelRegistry.keySet())) {
                        if (!location.getNamespace().equals(FullblockEnergistics.MODID)) {
                            continue;
                        }

                        modelRegistry.put(location, new AutoRotatingBakedModel(modelRegistry.get(location)));
                    }
                });
            }
        };
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientSetup);

        bus.addListener(FullEngDataGenerators::onGatherData);
    }
}
