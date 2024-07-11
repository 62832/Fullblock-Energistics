package gripe._90.fulleng;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.RegisterEvent;

import appeng.api.AECapabilities;
import appeng.api.ids.AECreativeTabIds;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.util.AEColor;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.core.AppEng;
import appeng.core.network.serverbound.PartLeftClickPacket;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.client.MonitorBlockEntityRenderer;
import gripe._90.fulleng.definition.FullEngBlockEntities;
import gripe._90.fulleng.definition.FullEngBlocks;
import gripe._90.fulleng.integration.Addons;
import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.integration.requester.RequesterTerminalMenu;

@Mod(FullblockEnergistics.MODID)
public class FullblockEnergistics {
    public static final String MODID = "fulleng";

    public static ResourceLocation makeId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public FullblockEnergistics(IEventBus modEventBus) {
        modEventBus.addListener(this::registerAll);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::addToCreativeTab);

        if (FMLEnvironment.dist.isClient()) {
            new Client(modEventBus);
        }
    }

    private void registerAll(RegisterEvent event) {
        FullEngBlocks.getBlocks().forEach(block -> {
            event.register(Registries.BLOCK, block.id(), block::block);
            event.register(Registries.ITEM, block.id(), block::asItem);
        });

        event.register(Registries.BLOCK_ENTITY_TYPE, helper -> FullEngBlockEntities.getBlockEntities()
                .forEach(helper::register));

        if (Addons.REQUESTER.isLoaded()) {
            event.register(
                    Registries.MENU, AppEng.makeId("requester_terminal_f"), () -> RequesterTerminalMenu.TYPE_FULLBLOCK);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var type : FullEngBlockEntities.getBlockEntities().values()) {
            event.registerBlockEntity(
                    AECapabilities.IN_WORLD_GRID_NODE_HOST, type, (be, context) -> (IInWorldGridNodeHost) be);
        }

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                FullEngBlockEntities.PATTERN_ENCODING_TERMINAL,
                (be, context) -> context != Direction.NORTH
                        ? be.getLogic().getBlankPatternInv().toItemHandler()
                        : null);
    }

    private void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(AECreativeTabIds.MAIN)) {
            FullEngBlocks.getBlocks().forEach(event::accept);
        }
    }

    private static class Client {
        private Client(IEventBus modEventBus) {
            modEventBus.addListener(this::initScreens);
            modEventBus.addListener(this::initBlockEntityRenders);
            modEventBus.addListener(this::registerBlockColourProviders);
            modEventBus.addListener(this::registerItemColourProviders);
            NeoForge.EVENT_BUS.addListener(this::addConversionMonitorHook);
        }

        private void initScreens(RegisterMenuScreensEvent event) {
            if (Addons.REQUESTER.isLoaded()) {
                RequesterIntegration.initScreen(event);
            }
        }

        private void initBlockEntityRenders(ModelEvent.RegisterGeometryLoaders ignoredEvent) {
            BlockEntityRenderers.register(FullEngBlockEntities.STORAGE_MONITOR, MonitorBlockEntityRenderer::new);
            BlockEntityRenderers.register(FullEngBlockEntities.CONVERSION_MONITOR, MonitorBlockEntityRenderer::new);
        }

        private void registerBlockColourProviders(RegisterColorHandlersEvent.Block event) {
            for (var block : FullEngBlocks.getBlocks()) {
                event.register(new ColorableBlockEntityBlockColor(), block.block());
            }
        }

        private void registerItemColourProviders(RegisterColorHandlersEvent.Item event) {
            for (var block : FullEngBlocks.getBlocks()) {
                event.register(new StaticItemColor(AEColor.TRANSPARENT), block.asItem());
            }
        }

        private void addConversionMonitorHook(PlayerInteractEvent.LeftClickBlock event) {
            var level = event.getLevel();

            if (level.isClientSide()) {
                if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)) {
                    return;
                }

                if (level.getBlockEntity(hitResult.getBlockPos()) instanceof ConversionMonitorBlockEntity) {
                    PacketDistributor.sendToServer(new PartLeftClickPacket(
                            hitResult, InteractionUtil.isInAlternateUseMode(event.getEntity())));
                    Objects.requireNonNull(Minecraft.getInstance().gameMode).destroyDelay = 5;
                    event.setCanceled(true);
                }
            }
        }
    }
}
