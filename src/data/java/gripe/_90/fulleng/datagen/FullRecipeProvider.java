package gripe._90.fulleng.datagen;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.definition.FullEngBlocks;
import gripe._90.fulleng.integration.Addons;

public class FullRecipeProvider extends RecipeProvider {
    public FullRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput output) {
        block(output, FullEngBlocks.TERMINAL);
        block(output, FullEngBlocks.CRAFTING_TERMINAL);
        block(output, FullEngBlocks.PATTERN_ENCODING_TERMINAL);
        block(output, FullEngBlocks.PATTERN_ACCESS_TERMINAL);

        block(output, FullEngBlocks.STORAGE_MONITOR);
        block(output, FullEngBlocks.CONVERSION_MONITOR);

        if (Addons.REQUESTER.isLoaded()) {
            block(Addons.REQUESTER.conditionalOutput(output), FullEngBlocks.REQUESTER_TERMINAL);
        }
    }

    private void block(@NotNull RecipeOutput output, BlockDefinition<? extends FullBlock<?>> block) {
        var part = block.block().getEquivalentPart();
        var partId = BuiltInRegistries.ITEM.getKey(part.asItem()).getPath();

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, block)
                .requires(part)
                .unlockedBy("has_" + partId, has(part))
                .save(output, FullblockEnergistics.makeId("terminals/block_" + partId + "_from_part"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, part)
                .requires(block)
                .unlockedBy("has_" + partId, has(part))
                .save(output, FullblockEnergistics.makeId("terminals/part_" + partId + "_from_block"));
    }
}
