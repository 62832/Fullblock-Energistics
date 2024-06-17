package gripe._90.fulleng.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import appeng.core.definitions.BlockDefinition;
import appeng.datagen.providers.models.AE2BlockStateProvider;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.block.MonitorBlock;
import gripe._90.fulleng.definition.FullEngBlocks;
import gripe._90.fulleng.integration.Addons;

public class FullModelProvider extends AE2BlockStateProvider {
    private static final ResourceLocation TERMINAL_OFF = FullblockEnergistics.makeId("block/terminal_off");

    public FullModelProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, FullblockEnergistics.MODID, existing);
    }

    @Override
    protected void registerStatesAndModels() {
        terminal(FullEngBlocks.TERMINAL, "ae2:part/terminal");
        terminal(FullEngBlocks.CRAFTING_TERMINAL, "ae2:part/crafting_terminal");
        terminal(FullEngBlocks.PATTERN_ENCODING_TERMINAL, "ae2:part/pattern_encoding_terminal");
        terminal(FullEngBlocks.PATTERN_ACCESS_TERMINAL, "ae2:part/pattern_access_terminal");

        monitor(FullEngBlocks.STORAGE_MONITOR, "ae2:part/storage_monitor");
        monitor(FullEngBlocks.CONVERSION_MONITOR, "ae2:part/conversion_monitor");

        if (Addons.REQUESTER.isLoaded()) {
            terminal(FullEngBlocks.REQUESTER_TERMINAL, "merequester:part/requester_terminal");
        }
    }

    private void terminal(BlockDefinition<?> terminal, String texturePrefix) {
        var existing = models().existingFileHelper;
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_bright"), ModelProvider.TEXTURE);
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_medium"), ModelProvider.TEXTURE);
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_dark"), ModelProvider.TEXTURE);

        var onModel = terminal != FullEngBlocks.TERMINAL
                ? models().withExistingParent(
                                "block/" + terminal.id().getPath(), FullblockEnergistics.MODID + ":block/terminal")
                        .texture("lightsBright", texturePrefix + "_bright")
                        .texture("lightsMedium", texturePrefix + "_medium")
                        .texture("lightsDark", texturePrefix + "_dark")
                        .renderType("cutout")
                : models().getExistingFile(FullblockEnergistics.makeId("block/terminal"));

        multiVariantGenerator(terminal, Variant.variant())
                .with(createFacingSpinDispatch())
                .with(PropertyDispatch.property(FullBlock.POWERED).generate(powered -> Variant.variant()
                        .with(VariantProperties.MODEL, powered ? onModel.getLocation() : TERMINAL_OFF)));
        simpleBlockItem(terminal.block(), onModel);
    }

    private void monitor(BlockDefinition<?> monitor, String texturePrefix) {
        var existing = models().existingFileHelper;
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_bright"), ModelProvider.TEXTURE);
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_medium"), ModelProvider.TEXTURE);
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_dark"), ModelProvider.TEXTURE);
        existing.trackGenerated(ResourceLocation.parse(texturePrefix + "_dark_locked"), ModelProvider.TEXTURE);

        var unlockedModel = models().withExistingParent(
                        "block/" + monitor.id().getPath(), FullblockEnergistics.MODID + ":block/terminal")
                .texture("lightsBright", texturePrefix + "_bright")
                .texture("lightsMedium", texturePrefix + "_medium")
                .texture("lightsDark", texturePrefix + "_dark")
                .renderType("cutout");
        var lockedModel = models().withExistingParent(
                        "block/" + monitor.id().getPath(), FullblockEnergistics.MODID + ":block/terminal")
                .texture("lightsBright", texturePrefix + "_bright")
                .texture("lightsMedium", texturePrefix + "_medium")
                .texture("lightsDark", texturePrefix + "_dark_locked")
                .renderType("cutout");

        multiVariantGenerator(monitor, Variant.variant())
                .with(createFacingSpinDispatch())
                .with(PropertyDispatch.properties(FullBlock.POWERED, MonitorBlock.LOCKED)
                        .generate((powered, locked) -> Variant.variant()
                                .with(
                                        VariantProperties.MODEL,
                                        !powered
                                                ? TERMINAL_OFF
                                                : locked ? lockedModel.getLocation() : unlockedModel.getLocation())));
        simpleBlockItem(monitor.block(), unlockedModel);
    }

    @Override
    public String getName() {
        return "Block States / Models";
    }
}
