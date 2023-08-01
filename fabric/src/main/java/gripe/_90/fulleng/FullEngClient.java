package gripe._90.fulleng;

import java.util.Objects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.IAEAddonEntrypoint;
import appeng.api.util.AEColor;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.render.ColorableBlockEntityBlockColor;
import appeng.client.render.StaticItemColor;
import appeng.core.definitions.BlockDefinition;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PartLeftClickPacket;
import appeng.init.client.InitScreens;
import appeng.util.InteractionUtil;

import gripe._90.fulleng.block.entity.monitor.ConversionMonitorBlockEntity;
import gripe._90.fulleng.client.renderer.MonitorBlockEntityRenderer;
import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.menu.PatternAccessTerminalMenu;

@SuppressWarnings("RedundantTypeArguments")
@Environment(EnvType.CLIENT)
public class FullEngClient implements IAEAddonEntrypoint {
    @Override
    public void onAe2Initialized() {
        initScreens();
        initBlockEntityRenderers();

        FullblockEnergistics.getBlocks().forEach(this::setRenderLayer);
        FullblockEnergistics.getBlocks().forEach(this::addColourProviders);

        AttackBlockCallback.EVENT.register(this::registerConversionMonitorHook);
    }

    private void initScreens() {
        InitScreens.<PatternAccessTerminalMenu, PatternAccessTermScreen<PatternAccessTerminalMenu>>register(
                PatternAccessTerminalMenu.TYPE_FULLBLOCK, PatternAccessTermScreen::new,
                "/screens/pattern_access_terminal.json");

        if (FullblockEnergistics.PLATFORM.isRequesterLoaded()) {
            RequesterIntegration.initScreen();
        }
    }

    private void initBlockEntityRenderers() {
        BlockEntityRenderers.register(FullblockEnergistics.STORAGE_MONITOR, MonitorBlockEntityRenderer::new);
        BlockEntityRenderers.register(FullblockEnergistics.CONVERSION_MONITOR, MonitorBlockEntityRenderer::new);
    }

    private void setRenderLayer(BlockDefinition<?> block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block.block(), RenderType.cutout());
    }

    private void addColourProviders(BlockDefinition<?> block) {
        ColorProviderRegistry.BLOCK.register(new ColorableBlockEntityBlockColor(), block.block());
        ColorProviderRegistry.ITEM.register(new StaticItemColor(AEColor.TRANSPARENT), block.block());
    }

    private InteractionResult registerConversionMonitorHook(Player player, Level level, InteractionHand hand,
            BlockPos pos, Direction direction) {
        if (level.isClientSide()) {
            if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)) {
                return InteractionResult.PASS;
            }

            if (level.getBlockEntity(hitResult.getBlockPos()) instanceof ConversionMonitorBlockEntity) {
                NetworkHandler.instance().sendToServer(
                        new PartLeftClickPacket(hitResult, InteractionUtil.isInAlternateUseMode(player)));
                Objects.requireNonNull(Minecraft.getInstance().gameMode).destroyDelay = 5;
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }
}
