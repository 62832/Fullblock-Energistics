package gripe._90.fulleng;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;
import appeng.core.definitions.ItemDefinition;

import gripe._90.fulleng.block.TerminalBlock;

public class FullEngDataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(ModelProvider::new);
        generator.addProvider(RecipeProvider::new);
        generator.addProvider(DropProvider::new);
        generator.addProvider(TagProvider::new);

        for (var en : List.of("en_us", "en_gb", "en_ca", "en_au", "en_nz")) {
            generator.addProvider(new LangProvider(generator, en));
        }
    }

    private static class ModelProvider extends FabricModelProvider {
        static final TextureSlot LIGHTS_BRIGHT = TextureSlot.create("lightsBright");
        static final TextureSlot LIGHTS_MEDIUM = TextureSlot.create("lightsMedium");
        static final TextureSlot LIGHTS_DARK = TextureSlot.create("lightsDark");

        static final ModelTemplate TERMINAL = new ModelTemplate(
                Optional.of(FullblockEnergistics.makeId("block/terminal")), Optional.empty(), LIGHTS_BRIGHT,
                LIGHTS_MEDIUM, LIGHTS_DARK);

        public ModelProvider(FabricDataGenerator gen) {
            super(gen);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators gen) {
            terminal(gen, FullblockEnergistics.TERMINAL_BLOCK, "ae2:part/terminal");
            terminal(gen, FullblockEnergistics.CRAFTING_TERMINAL_BLOCK, "ae2:part/crafting_terminal");
            terminal(gen, FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BLOCK, "ae2:part/pattern_encoding_terminal");
            terminal(gen, FullblockEnergistics.PATTERN_ACCESS_TERMINAL_BLOCK, "ae2:part/pattern_access_terminal");
            terminal(gen, RequesterIntegration.REQUESTER_TERMINAL_BLOCK, "merequester:part/requester_terminal");
        }

        @Override
        public void generateItemModels(ItemModelGenerators gen) {
        }

        private void terminal(BlockModelGenerators gen, BlockDefinition<?> terminal, String texturePrefix) {
            var offModel = FullblockEnergistics.makeId("block/terminal_off");
            var onModel = terminal == FullblockEnergistics.TERMINAL_BLOCK
                    ? FullblockEnergistics.makeId("block/terminal")
                    : TERMINAL.create(terminal.block(), new TextureMapping()
                            .put(LIGHTS_BRIGHT, new ResourceLocation(texturePrefix + "_bright"))
                            .put(LIGHTS_MEDIUM, new ResourceLocation(texturePrefix + "_medium"))
                            .put(LIGHTS_DARK, new ResourceLocation(texturePrefix + "_dark")),
                            gen.modelOutput);
            gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(terminal.block())
                    .with(PropertyDispatch.property(TerminalBlock.POWERED)
                            .select(false, Variant.variant().with(VariantProperties.MODEL, offModel))
                            .select(true, Variant.variant().with(VariantProperties.MODEL, onModel))));
        }
    }

    private static class RecipeProvider extends FabricRecipeProvider {
        public RecipeProvider(FabricDataGenerator gen) {
            super(gen);
        }

        @Override
        protected void generateRecipes(Consumer<FinishedRecipe> consumer) {
            terminal(consumer, FullblockEnergistics.TERMINAL_BLOCK, AEParts.TERMINAL);
            terminal(consumer, FullblockEnergistics.CRAFTING_TERMINAL_BLOCK, AEParts.CRAFTING_TERMINAL);
            terminal(consumer, FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BLOCK, AEParts.PATTERN_ENCODING_TERMINAL);
            terminal(consumer, FullblockEnergistics.PATTERN_ACCESS_TERMINAL_BLOCK, AEParts.PATTERN_ACCESS_TERMINAL);
        }

        private void terminal(Consumer<FinishedRecipe> consumer, BlockDefinition<?> block, ItemDefinition<?> part) {
            var partId = part.id().getPath();
            ShapelessRecipeBuilder.shapeless(block).requires(part).unlockedBy("has_" + partId, has(part))
                    .save(consumer, FullblockEnergistics.makeId("terminals/block_" + partId + "_from_part"));
            ShapelessRecipeBuilder.shapeless(part).requires(block).unlockedBy("has_" + partId, has(part))
                    .save(consumer, FullblockEnergistics.makeId("terminals/part_" + partId + "_from_block"));
        }
    }

    private static class DropProvider extends SimpleFabricLootTableProvider {
        public DropProvider(FabricDataGenerator gen) {
            super(gen, LootContextParamSets.BLOCK);
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            FullblockEnergistics.getBlocks()
                    .forEach(b -> consumer.accept(FullblockEnergistics.makeId("blocks/" + b.id().getPath()),
                            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(b)).when(ExplosionCondition.survivesExplosion()))));
        }
    }

    private static class TagProvider extends FabricTagProvider.BlockTagProvider {
        public TagProvider(FabricDataGenerator gen) {
            super(gen);
        }

        @Override
        protected void generateTags() {
            FullblockEnergistics.getBlocks().forEach(b -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(b.block()));
        }
    }

    private static class LangProvider extends FabricLanguageProvider {
        protected LangProvider(FabricDataGenerator gen, String lang) {
            super(gen, lang);
        }

        @Override
        public void generateTranslations(TranslationBuilder builder) {
            FullblockEnergistics.getBlocks().forEach(b -> builder.add(b.block(), b.getEnglishName()));
        }
    }
}
