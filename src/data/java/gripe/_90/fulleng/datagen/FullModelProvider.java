package gripe._90.fulleng.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import appeng.datagen.providers.models.AE2BlockStateProvider;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.block.MonitorBlock;

public class FullModelProvider extends AE2BlockStateProvider {
    private static final ResourceLocation TERMINAL_OFF =
            ResourceLocation.fromNamespaceAndPath(FullblockEnergistics.MODID, "block/terminal_off");

    public FullModelProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, FullblockEnergistics.MODID, existing);
    }

    @Override
    protected void registerStatesAndModels() {
        terminal(FullblockEnergistics.TERMINAL, "ae2:part/terminal");
        terminal(FullblockEnergistics.CRAFTING_TERMINAL, "ae2:part/crafting_terminal");
        terminal(FullblockEnergistics.PATTERN_ENCODING_TERMINAL, "ae2:part/pattern_encoding_terminal");
        terminal(FullblockEnergistics.PATTERN_ACCESS_TERMINAL, "ae2:part/pattern_access_terminal");

        monitor(FullblockEnergistics.STORAGE_MONITOR, "ae2:part/storage_monitor");
        monitor(FullblockEnergistics.CONVERSION_MONITOR, "ae2:part/conversion_monitor");

        terminal(FullblockEnergistics.REQUESTER_TERMINAL, "merequester:part/requester_terminal");
        terminal(FullblockEnergistics.EXTENDED_PATTERN_ACCESS_TERMINAL, "extendedae:part/ex_pattern_access_terminal");
    }

    private void terminal(DeferredBlock<?> terminal, String lightsTexture) {
        var onModel = terminal != FullblockEnergistics.TERMINAL
                ? models().withExistingParent(
                                "block/" + terminal.getId().getPath(), FullblockEnergistics.MODID + ":block/terminal")
                        .texture("lightsBright", lightsTexture + "_bright")
                        .texture("lightsMedium", lightsTexture + "_medium")
                        .texture("lightsDark", lightsTexture + "_dark")
                        .renderType("cutout")
                : models().getExistingFile(
                                ResourceLocation.fromNamespaceAndPath(FullblockEnergistics.MODID, "block/terminal"));

        var builder = MultiVariantGenerator.multiVariant(terminal.get())
                .with(createFacingSpinDispatch())
                .with(PropertyDispatch.property(FullBlock.POWERED).generate(powered -> Variant.variant()
                        .with(VariantProperties.MODEL, powered ? onModel.getLocation() : TERMINAL_OFF)));
        registeredBlocks.put(terminal.get(), builder.get()::getAsJsonObject);
        simpleBlockItem(terminal.get(), onModel);
    }

    private void monitor(DeferredBlock<?> monitor, String texturePrefix) {
        var unlockedModel = models().withExistingParent(
                        "block/" + monitor.getId().getPath(), FullblockEnergistics.MODID + ":block/terminal")
                .texture("lightsBright", texturePrefix + "_bright")
                .texture("lightsMedium", texturePrefix + "_medium")
                .renderType("cutout");
        var lockedModel = models().withExistingParent(
                        "block/" + monitor.getId().getPath(), FullblockEnergistics.MODID + ":block/terminal")
                .texture("lightsBright", texturePrefix + "_bright")
                .texture("lightsMedium", texturePrefix + "_medium")
                .texture("lightsDark", texturePrefix + "_dark_locked")
                .renderType("cutout");

        var builder = MultiVariantGenerator.multiVariant(monitor.get())
                .with(createFacingSpinDispatch())
                .with(PropertyDispatch.properties(FullBlock.POWERED, MonitorBlock.LOCKED)
                        .generate((powered, locked) -> Variant.variant()
                                .with(
                                        VariantProperties.MODEL,
                                        !powered
                                                ? TERMINAL_OFF
                                                : (locked ? lockedModel : unlockedModel).getLocation())));
        registeredBlocks.put(monitor.get(), builder.get()::getAsJsonObject);
        simpleBlockItem(monitor.get(), unlockedModel);
    }

    @Override
    public String getName() {
        return "Block States / Models";
    }
}
