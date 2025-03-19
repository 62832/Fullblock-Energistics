package gripe._90.fulleng.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.registries.DeferredBlock;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.integration.Addons;

public class FullRecipeProvider extends RecipeProvider {
    public FullRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput output) {
        block(output, FullblockEnergistics.TERMINAL);
        block(output, FullblockEnergistics.CRAFTING_TERMINAL);
        block(output, FullblockEnergistics.PATTERN_ENCODING_TERMINAL);
        block(output, FullblockEnergistics.PATTERN_ACCESS_TERMINAL);

        block(output, FullblockEnergistics.STORAGE_MONITOR);
        block(output, FullblockEnergistics.CONVERSION_MONITOR);

        block(conditionalOutput(output, Addons.MEREQUESTER), FullblockEnergistics.REQUESTER_TERMINAL);
        block(conditionalOutput(output, Addons.EXTENDEDAE), FullblockEnergistics.EXTENDED_PATTERN_ACCESS_TERMINAL);
        block(conditionalOutput(output, Addons.APPLIEDE), FullblockEnergistics.TRANSMUTATION_TERMINAL);
    }

    private void block(RecipeOutput output, DeferredBlock<? extends FullBlock<?>> block) {
        var part = block.get().getEquivalentPart();
        var partId = BuiltInRegistries.ITEM.getKey(part.asItem()).getPath();

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, block)
                .requires(part)
                .unlockedBy("has_" + partId, has(part))
                .save(
                        output,
                        ResourceLocation.fromNamespaceAndPath(
                                FullblockEnergistics.MODID, "terminals/block_" + partId + "_from_part"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, part)
                .requires(block)
                .unlockedBy("has_" + partId, has(part))
                .save(
                        output,
                        ResourceLocation.fromNamespaceAndPath(
                                FullblockEnergistics.MODID, "terminals/part_" + partId + "_from_block"));
    }

    private RecipeOutput conditionalOutput(RecipeOutput output, Addons addon) {
        return output.withConditions(new ModLoadedCondition(addon.getModId()));
    }
}
