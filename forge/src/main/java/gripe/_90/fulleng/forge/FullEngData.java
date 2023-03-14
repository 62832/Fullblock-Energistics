package gripe._90.fulleng.forge;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;
import appeng.core.definitions.ItemDefinition;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.RequesterIntegration;
import gripe._90.fulleng.block.TerminalBlock;

@Mod.EventBusSubscriber(modid = FullblockEnergistics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FullEngData {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existing = event.getExistingFileHelper();

        generator.addProvider(true, new ModelProvider(generator, existing));
        generator.addProvider(true, new StateProvider(generator, existing));
        generator.addProvider(true, new RecipeProvider(generator));
        generator.addProvider(true, new DropProvider(generator));
        generator.addProvider(true, new TagProvider(generator, existing));
    }

    public static final class StateProvider extends BlockStateProvider {
        private final ExistingFileHelper efh;

        public StateProvider(DataGenerator gen, ExistingFileHelper efh) {
            super(gen, FullblockEnergistics.MODID, efh);
            this.efh = efh;
        }

        @Override
        protected void registerStatesAndModels() {
            FullblockEnergistics.getBlocks().forEach(this::terminal);
        }

        private void terminal(BlockDefinition<?> terminal) {
            getVariantBuilder(terminal.block())
                    .partialState().with(TerminalBlock.POWERED, false)
                    .setModels(new ConfiguredModel(new ModelFile.ExistingModelFile(
                            FullblockEnergistics.makeId("block/terminal_off"), efh)))
                    .partialState().with(TerminalBlock.POWERED, true)
                    .setModels(new ConfiguredModel(new ModelFile.ExistingModelFile(
                            FullblockEnergistics.makeId("block/" + terminal.id().getPath()), efh)));
            itemModels().withExistingParent(terminal.id().getPath(),
                    FullblockEnergistics.makeId("block/" + terminal.id().getPath()));
        }
    }

    public static final class ModelProvider extends BlockModelProvider {
        private final ExistingFileHelper efh;

        public ModelProvider(DataGenerator gen, ExistingFileHelper efh) {
            super(gen, FullblockEnergistics.MODID, efh);
            this.efh = efh;
        }

        @Override
        protected void registerModels() {
            terminal(FullblockEnergistics.TERMINAL_BLOCK, "ae2:part/terminal");
            terminal(FullblockEnergistics.CRAFTING_TERMINAL_BLOCK, "ae2:part/crafting_terminal");
            terminal(FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BLOCK, "ae2:part/pattern_encoding_terminal");
            terminal(FullblockEnergistics.PATTERN_ACCESS_TERMINAL_BLOCK, "ae2:part/pattern_access_terminal");
            terminal(RequesterIntegration.REQUESTER_TERMINAL_BLOCK, "merequester:part/requester_terminal");
        }

        private void terminal(BlockDefinition<?> terminal, String texturePrefix) {
            var baseTerminal = FullblockEnergistics.makeId("block/terminal");
            var lightsBright = new ResourceLocation(texturePrefix + "_bright");
            var lightsMedium = new ResourceLocation(texturePrefix + "_medium");
            var lightsDark = new ResourceLocation(texturePrefix + "_dark");

            efh.trackGenerated(lightsBright, TEXTURE);
            efh.trackGenerated(lightsMedium, TEXTURE);
            efh.trackGenerated(lightsDark, TEXTURE);

            if (terminal != FullblockEnergistics.TERMINAL_BLOCK) {
                withExistingParent(terminal.id().getPath(), baseTerminal)
                        .texture("lightsBright", lightsBright)
                        .texture("lightsMedium", lightsMedium)
                        .texture("lightsDark", lightsDark);
            }
        }
    }

    public static final class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
        public RecipeProvider(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
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

    public static final class DropProvider extends LootTableProvider {
        public DropProvider(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return ImmutableList.of(Pair.of(BlockDrops::new, LootContextParamSets.BLOCK));
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext tracker) {
            map.forEach((id, table) -> LootTables.validate(tracker, id, table));
        }

        private static final class BlockDrops extends BlockLoot {
            @Override
            protected void addTables() {
                for (var b : getKnownBlocks()) {
                    add(b, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(b)).when(ExplosionCondition.survivesExplosion())));
                }
            }

            @Override
            protected @NotNull Iterable<Block> getKnownBlocks() {
                return FullblockEnergistics.getBlocks().stream().map(BlockDefinition::block)
                        .map(Block.class::cast)::iterator;
            }
        }
    }

    public static final class TagProvider extends BlockTagsProvider {
        public TagProvider(DataGenerator arg, @Nullable ExistingFileHelper efh) {
            super(arg, FullblockEnergistics.MODID, efh);
        }

        @Override
        protected void addTags() {
            FullblockEnergistics.getBlocks().forEach(b -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(b.block()));
        }
    }
}
