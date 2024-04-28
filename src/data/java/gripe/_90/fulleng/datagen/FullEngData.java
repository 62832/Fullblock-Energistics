package gripe._90.fulleng.datagen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import gripe._90.fulleng.FullblockEnergistics;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = FullblockEnergistics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FullEngData {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new FullRecipeProvider(output));
        generator.addProvider(event.includeServer(), new FullDropProvider(output));
        generator.addProvider(event.includeServer(), new FullTagProvider(output, event.getLookupProvider()));

        generator.addProvider(event.includeClient(), new FullLanguageProvider(output));
        generator.addProvider(event.includeClient(), new FullModelProvider(output, event.getExistingFileHelper()));
    }
}
