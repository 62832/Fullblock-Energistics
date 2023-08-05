package gripe._90.fulleng;

import java.util.Optional;

import com.google.gson.JsonPrimitive;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.blockstates.VariantProperty;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import appeng.api.orientation.BlockOrientation;
import appeng.api.orientation.IOrientationStrategy;
import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.definition.FullEngBlocks;

class ModelProvider extends FabricModelProvider {
    private static final VariantProperty<VariantProperties.Rotation> Z_ROT =
            new VariantProperty<>("ae2:z", r -> new JsonPrimitive(r.ordinal() * 90));

    private static final TextureSlot LIGHTS_BRIGHT = TextureSlot.create("lightsBright");
    private static final TextureSlot LIGHTS_MEDIUM = TextureSlot.create("lightsMedium");
    private static final TextureSlot LIGHTS_DARK = TextureSlot.create("lightsDark");

    private static final ModelTemplate TERMINAL = new ModelTemplate(
            Optional.of(FullblockEnergistics.makeId("block/terminal")),
            Optional.empty(),
            LIGHTS_BRIGHT,
            LIGHTS_MEDIUM,
            LIGHTS_DARK);
    private static final ResourceLocation TERMINAL_OFF = FullblockEnergistics.makeId("block/terminal_off");

    ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        terminal(gen, FullEngBlocks.TERMINAL, "ae2:part/terminal");
        terminal(gen, FullEngBlocks.CRAFTING_TERMINAL, "ae2:part/crafting_terminal");
        terminal(gen, FullEngBlocks.PATTERN_ENCODING_TERMINAL, "ae2:part/pattern_encoding_terminal");
        terminal(gen, FullEngBlocks.PATTERN_ACCESS_TERMINAL, "ae2:part/pattern_access_terminal");
        terminal(gen, FullEngBlocks.REQUESTER_TERMINAL, "merequester:part/requester_terminal");

        monitor(gen, FullEngBlocks.STORAGE_MONITOR, "ae2:part/storage_monitor");
        monitor(gen, FullEngBlocks.CONVERSION_MONITOR, "ae2:part/conversion_monitor");
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {}

    private void terminal(BlockModelGenerators gen, BlockDefinition<?> terminal, String texturePrefix) {
        var onModel = terminal == FullEngBlocks.TERMINAL
                ? FullblockEnergistics.makeId("block/terminal")
                : TERMINAL.create(
                        FullblockEnergistics.makeId("block/" + terminal.id().getPath()),
                        new TextureMapping()
                                .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                                .put(LIGHTS_MEDIUM, new ResourceLocation(texturePrefix + "_medium"))
                                .put(LIGHTS_DARK, new ResourceLocation(texturePrefix + "_dark")),
                        gen.modelOutput);

        gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(terminal.block())
                .with(PropertyDispatch.properties(
                                BlockStateProperties.FACING, IOrientationStrategy.SPIN, FullBlock.POWERED)
                        .generate((facing, spin, powered) -> {
                            var orientation = BlockOrientation.get(facing, spin);
                            var variant =
                                    Variant.variant().with(VariantProperties.MODEL, powered ? onModel : TERMINAL_OFF);

                            return applyRotation(
                                    variant, orientation.getAngleX(), orientation.getAngleY(), orientation.getAngleZ());
                        })));
    }

    private void monitor(BlockModelGenerators gen, BlockDefinition<?> monitor, String texturePrefix) {
        var storage = monitor == FullEngBlocks.STORAGE_MONITOR;
        var unlockedModel = TERMINAL.create(
                FullblockEnergistics.makeId("block/" + monitor.id().getPath()),
                new TextureMapping()
                        .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                        .put(LIGHTS_MEDIUM, new ResourceLocation(texturePrefix + "_medium"))
                        .put(LIGHTS_DARK, new ResourceLocation(texturePrefix + "_dark")),
                gen.modelOutput);
        var lockedModel = TERMINAL.create(
                FullblockEnergistics.makeId("block/" + monitor.id().getPath() + "_locked"),
                new TextureMapping()
                        .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                        .put(
                                LIGHTS_MEDIUM,
                                new ResourceLocation(texturePrefix + "_medium" + (storage ? "" : "_locked")))
                        .put(LIGHTS_DARK, new ResourceLocation(texturePrefix + "_dark" + (storage ? "_locked" : ""))),
                gen.modelOutput);

        gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(monitor.block())
                .with(PropertyDispatch.properties(
                                BlockStateProperties.FACING,
                                IOrientationStrategy.SPIN,
                                FullBlock.POWERED,
                                MonitorBlock.LOCKED)
                        .generate((facing, spin, powered, locked) -> {
                            var orientation = BlockOrientation.get(facing, spin);
                            var variant = Variant.variant()
                                    .with(
                                            VariantProperties.MODEL,
                                            !powered ? TERMINAL_OFF : locked ? lockedModel : unlockedModel);

                            return applyRotation(
                                    variant, orientation.getAngleX(), orientation.getAngleY(), orientation.getAngleZ());
                        })));
    }

    private Variant applyRotation(Variant variant, int angleX, int angleY, int angleZ) {
        angleX = normalizeAngle(angleX);
        angleY = normalizeAngle(angleY);
        angleZ = normalizeAngle(angleZ);

        if (angleX != 0) {
            variant = variant.with(VariantProperties.X_ROT, rotationByAngle(angleX));
        }
        if (angleY != 0) {
            variant = variant.with(VariantProperties.Y_ROT, rotationByAngle(angleY));
        }
        if (angleZ != 0) {
            variant = variant.with(Z_ROT, rotationByAngle(angleZ));
        }
        return variant;
    }

    private int normalizeAngle(int angle) {
        return angle - (angle / 360) * 360;
    }

    private VariantProperties.Rotation rotationByAngle(int angle) {
        return switch (angle) {
            case 0 -> VariantProperties.Rotation.R0;
            case 90 -> VariantProperties.Rotation.R90;
            case 180 -> VariantProperties.Rotation.R180;
            case 270 -> VariantProperties.Rotation.R270;
            default -> throw new IllegalArgumentException("Invalid angle: " + angle);
        };
    }
}
