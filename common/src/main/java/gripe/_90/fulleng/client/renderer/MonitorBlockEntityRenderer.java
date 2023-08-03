package gripe._90.fulleng.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import appeng.client.render.BlockEntityRenderHelper;

import gripe._90.fulleng.block.entity.monitor.MonitorBlockEntity;

@Environment(EnvType.CLIENT)
public class MonitorBlockEntityRenderer<E extends MonitorBlockEntity> implements BlockEntityRenderer<E> {
    public MonitorBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredContext) {}

    @Override
    public void render(
            MonitorBlockEntity be,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffers,
            int combinedLightIn,
            int combinedOverlayIn) {
        if (!be.isActive() || be.getDisplayed() == null) {
            return;
        }

        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);
        BlockEntityRenderHelper.rotateToFace(poseStack, be.getOrientation());

        poseStack.translate(0, 0.05, 0.5);
        BlockEntityRenderHelper.renderItem2dWithAmount(
                poseStack,
                buffers,
                be.getDisplayed(),
                be.getAmount(),
                be.canCraft(),
                0.4f,
                -0.23f,
                be.getColor().contrastTextColor,
                be.getLevel());

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 900;
    }
}
