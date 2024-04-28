package gripe._90.fulleng.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.definition.FullEngBlocks;

public class FullTagProvider extends BlockTagsProvider {
    public FullTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, FullblockEnergistics.MODID, null);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        FullEngBlocks.getBlocks().forEach(b -> tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BuiltInRegistries.BLOCK.getResourceKey(b.block()).orElseThrow()));
    }

    @NotNull
    @Override
    public String getName() {
        return "Tags";
    }
}
