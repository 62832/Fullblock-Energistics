package gripe._90.fulleng;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.integration.tooltips.TooltipProvider;

class LocalisationProvider extends FabricLanguageProvider {
    LocalisationProvider(FabricDataGenerator gen) {
        super(gen);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(RequesterIntegration.NOT_INSTALLED.getString(), "ME Requester not installed.");
        builder.add("config.jade.plugin_fulleng." + TooltipProvider.MONITOR, "Fullblock Storage Monitor");
    }
}
