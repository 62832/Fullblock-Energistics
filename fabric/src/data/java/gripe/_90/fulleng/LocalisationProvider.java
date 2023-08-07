package gripe._90.fulleng;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import gripe._90.fulleng.integration.requester.RequesterIntegration;
import gripe._90.fulleng.integration.tooltips.TooltipProvider;

class LocalisationProvider extends FabricLanguageProvider {
    LocalisationProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(RequesterIntegration.NOT_INSTALLED.getString(), "ME Requester not installed.");
        builder.add("config.jade.plugin_fulleng." + TooltipProvider.STORAGE_MONITOR, "Fullblock Storage Monitor");
    }
}
