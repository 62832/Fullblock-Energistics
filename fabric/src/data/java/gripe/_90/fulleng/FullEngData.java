package gripe._90.fulleng;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
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

import appeng.core.AppEng;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;
import appeng.core.definitions.ItemDefinition;

import gripe._90.fulleng.block.FullBlock;
import gripe._90.fulleng.block.MonitorBlock;

@SuppressWarnings("unused")
public class FullEngData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(ModelProvider::new);
        generator.addProvider(RecipeProvider::new);
        generator.addProvider(DropProvider::new);
        generator.addProvider(TagProvider::new);
    }

    private static class ModelProvider extends FabricModelProvider {
        private static final TextureSlot LIGHTS_BRIGHT = TextureSlot.create("lightsBright");
        private static final TextureSlot LIGHTS_MEDIUM = TextureSlot.create("lightsMedium");
        private static final TextureSlot LIGHTS_DARK = TextureSlot.create("lightsDark");

        private static final ModelTemplate TERMINAL = new ModelTemplate(
                Optional.of(AppEng.makeId("block/terminal")), Optional.empty(), LIGHTS_BRIGHT,
                LIGHTS_MEDIUM, LIGHTS_DARK);
        private static final ResourceLocation TERMINAL_OFF = AppEng.makeId("block/terminal_off");

        public ModelProvider(FabricDataGenerator gen) {
            super(gen);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators gen) {
            terminal(gen, FullblockEnergistics.TERMINAL_BLOCK, "ae2:part/terminal");
            terminal(gen, FullblockEnergistics.CRAFTING_TERMINAL_BLOCK, "ae2:part/crafting_terminal");
            terminal(gen, FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BLOCK, "ae2:part/pattern_encoding_terminal");
            terminal(gen, FullblockEnergistics.PATTERN_ACCESS_TERMINAL_BLOCK, "ae2:part/pattern_access_terminal");
            terminal(gen, FullblockEnergistics.REQUESTER_TERMINAL_BLOCK, "merequester:part/requester_terminal");

            monitor(gen, FullblockEnergistics.STORAGE_MONITOR_BLOCK, "ae2:part/storage_monitor");
            monitor(gen, FullblockEnergistics.CONVERSION_MONITOR_BLOCK, "ae2:part/conversion_monitor");
        }

        @Override
        public void generateItemModels(ItemModelGenerators gen) {
        }

        private void terminal(BlockModelGenerators gen, BlockDefinition<?> terminal, String texturePrefix) {
            var onModel = terminal == FullblockEnergistics.TERMINAL_BLOCK
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
            var storage = monitor == FullblockEnergistics.STORAGE_MONITOR_BLOCK;
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

    private static class RecipeProvider extends FabricRecipeProvider {
        public RecipeProvider(FabricDataGenerator gen) {
            super(gen);
        }

        @Override
        protected void generateRecipes(Consumer<FinishedRecipe> consumer) {
            block(consumer, FullblockEnergistics.TERMINAL_BLOCK, AEParts.TERMINAL);
            block(consumer, FullblockEnergistics.CRAFTING_TERMINAL_BLOCK, AEParts.CRAFTING_TERMINAL);
            block(consumer, FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BLOCK, AEParts.PATTERN_ENCODING_TERMINAL);
            block(consumer, FullblockEnergistics.PATTERN_ACCESS_TERMINAL_BLOCK, AEParts.PATTERN_ACCESS_TERMINAL);

            block(consumer, FullblockEnergistics.STORAGE_MONITOR_BLOCK, AEParts.STORAGE_MONITOR);
            block(consumer, FullblockEnergistics.CONVERSION_MONITOR_BLOCK, AEParts.CONVERSION_MONITOR);
        }

        private void block(Consumer<FinishedRecipe> consumer, BlockDefinition<?> block, ItemDefinition<?> part) {
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
}
