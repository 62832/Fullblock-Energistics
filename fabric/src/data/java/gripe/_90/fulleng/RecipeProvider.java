package gripe._90.fulleng;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

import appeng.core.definitions.BlockDefinition;

import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.definition.FullEngBlocks;

class RecipeProvider extends FabricRecipeProvider {
    RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        block(consumer, FullEngBlocks.TERMINAL);
        block(consumer, FullEngBlocks.CRAFTING_TERMINAL);
        block(consumer, FullEngBlocks.PATTERN_ENCODING_TERMINAL);
        block(consumer, FullEngBlocks.PATTERN_ACCESS_TERMINAL);

        block(consumer, FullEngBlocks.STORAGE_MONITOR);
        block(consumer, FullEngBlocks.CONVERSION_MONITOR);
    }

    private void block(Consumer<FinishedRecipe> consumer, BlockDefinition<? extends FullBlock<?>> block) {
        var part = block.block().getEquivalentPart();
        var partId = part.id().getPath();

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, block)
                .requires(part)
                .unlockedBy("has_" + partId, has(part))
                .save(consumer, FullblockEnergistics.makeId("terminals/block_" + partId + "_from_part"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, part)
                .requires(block)
                .unlockedBy("has_" + partId, has(part))
                .save(consumer, FullblockEnergistics.makeId("terminals/part_" + partId + "_from_block"));
    }
}
