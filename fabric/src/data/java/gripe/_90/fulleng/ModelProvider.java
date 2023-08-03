package gripe._90.fulleng;

import java.util.Optional;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import appeng.core.AppEng;
import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.definition.FullEngBlocks;

class ModelProvider extends FabricModelProvider {
    private static final TextureSlot LIGHTS_BRIGHT = TextureSlot.create("lightsBright");
    private static final TextureSlot LIGHTS_MEDIUM = TextureSlot.create("lightsMedium");
    private static final TextureSlot LIGHTS_DARK = TextureSlot.create("lightsDark");

    private static final ModelTemplate TERMINAL = new ModelTemplate(
            Optional.of(AppEng.makeId("block/terminal")), Optional.empty(), LIGHTS_BRIGHT,
            LIGHTS_MEDIUM, LIGHTS_DARK);
    private static final ResourceLocation TERMINAL_OFF = AppEng.makeId("block/terminal_off");

    ModelProvider(FabricDataGenerator gen) {
        super(gen);
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
    public void generateItemModels(ItemModelGenerators gen) {
    }

    private void terminal(BlockModelGenerators gen, BlockDefinition<?> terminal, String texturePrefix) {
        var onModel = terminal == FullEngBlocks.TERMINAL
                ? AppEng.makeId("block/terminal")
                : TERMINAL.create(AppEng.makeId("block/" + terminal.id().getPath()), new TextureMapping()
                        .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                        .put(LIGHTS_MEDIUM, new ResourceLocation(texturePrefix + "_medium"))
                        .put(LIGHTS_DARK, new ResourceLocation(texturePrefix + "_dark")),
                        gen.modelOutput);
        gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(terminal.block())
                .with(PropertyDispatch.property(FullBlock.POWERED)
                        .select(false, Variant.variant().with(VariantProperties.MODEL, TERMINAL_OFF))
                        .select(true, Variant.variant().with(VariantProperties.MODEL, onModel))));
        gen.delegateItemModel(terminal.block(), onModel);
    }

    private void monitor(BlockModelGenerators gen, BlockDefinition<?> monitor, String texturePrefix) {
        var storage = monitor == FullEngBlocks.STORAGE_MONITOR;
        var unlockedModel = TERMINAL.create(AppEng.makeId("block/" + monitor.id().getPath()), new TextureMapping()
                .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                .put(LIGHTS_MEDIUM, new ResourceLocation(texturePrefix + "_medium"))
                .put(LIGHTS_DARK, new ResourceLocation(texturePrefix + "_dark")),
                gen.modelOutput);
        var lockedModel = TERMINAL.create(
                AppEng.makeId("block/" + monitor.id().getPath() + "_locked"), new TextureMapping()
                        .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                        .put(LIGHTS_MEDIUM,
                                new ResourceLocation(texturePrefix + "_medium" + (storage ? "" : "_locked")))
                        .put(LIGHTS_DARK,
                                new ResourceLocation(texturePrefix + "_dark" + (storage ? "_locked" : ""))),
                gen.modelOutput);
        gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(monitor.block())
                .with(PropertyDispatch.properties(FullBlock.POWERED, MonitorBlock.LOCKED)
                        .generate((powered, locked) -> Variant.variant().with(VariantProperties.MODEL,
                                !powered ? TERMINAL_OFF : locked ? lockedModel : unlockedModel))));
        gen.delegateItemModel(monitor.block(), unlockedModel);
    }
}
