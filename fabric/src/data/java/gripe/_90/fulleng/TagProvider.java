package gripe._90.fulleng;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.tags.BlockTags;

import gripe._90.fulleng.definition.FullEngBlocks;

class TagProvider extends FabricTagProvider.BlockTagProvider {
    TagProvider(FabricDataOutput output) {
        super(output, CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor()));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        FullEngBlocks.getBlocks().forEach(b -> tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BuiltInRegistries.BLOCK.getResourceKey(b.block()).orElseThrow()));
    }
}
