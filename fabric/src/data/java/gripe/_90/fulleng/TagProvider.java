package gripe._90.fulleng;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tags.BlockTags;

class TagProvider extends FabricTagProvider.BlockTagProvider {
    public TagProvider(FabricDataGenerator gen) {
        super(gen);
    }

    @Override
    protected void generateTags() {
        FullblockEnergistics.getBlocks().forEach(b -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(b.block()));
    }
}
