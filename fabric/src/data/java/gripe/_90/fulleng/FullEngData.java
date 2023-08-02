package gripe._90.fulleng;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@SuppressWarnings("unused")
public class FullEngData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(ModelProvider::new);
        generator.addProvider(RecipeProvider::new);
        generator.addProvider(DropProvider::new);
        generator.addProvider(TagProvider::new);
        generator.addProvider(LocalisationProvider::new);
    }
}
