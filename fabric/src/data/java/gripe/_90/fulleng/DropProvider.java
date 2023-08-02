package gripe._90.fulleng;

import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

class DropProvider extends SimpleFabricLootTableProvider {
    DropProvider(FabricDataOutput output) {
        super(output, LootContextParamSets.BLOCK);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        FullblockEnergistics.getBlocks()
                .forEach(b -> consumer.accept(FullblockEnergistics.makeId("blocks/" + b.id().getPath()),
                        LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(b)).when(ExplosionCondition.survivesExplosion()))));
    }
}
