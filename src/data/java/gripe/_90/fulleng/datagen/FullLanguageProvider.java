package gripe._90.fulleng.datagen;

import org.jetbrains.annotations.NotNull;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.integration.Addons;
import gripe._90.fulleng.integration.tooltips.TooltipProvider;

public class FullLanguageProvider extends LanguageProvider {
    public FullLanguageProvider(PackOutput output) {
        super(output, FullblockEnergistics.MODID, "en_us");
    }

    @Override
    public void addTranslations() {
        add("config.jade.plugin_fulleng." + TooltipProvider.STORAGE_MONITOR, "Fullblock Storage Monitor");

        for (var addon : Addons.values()) {
            add(addon.getNotInstalledTooltip().getString(), addon.getModName() + " not installed.");
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "Language";
    }
}
