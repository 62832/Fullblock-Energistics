package gripe._90.fulleng.datagen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import gripe._90.fulleng.FullblockEnergistics;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = FullblockEnergistics.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FullEngData {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new FullDropProvider(output, registries));
        generator.addProvider(event.includeServer(), new FullRecipeProvider(output, registries));
        generator.addProvider(event.includeServer(), new FullTagProvider(output, registries));

        generator.addProvider(event.includeClient(), new FullLanguageProvider(output));
        generator.addProvider(event.includeClient(), new FullModelProvider(output, event.getExistingFileHelper()));
    }
}
