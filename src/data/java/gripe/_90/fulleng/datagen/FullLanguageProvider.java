package gripe._90.fulleng.datagen;

import org.jetbrains.annotations.NotNull;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import gripe._90.fulleng.FullblockEnergistics;

public class FullLanguageProvider extends LanguageProvider {
    public FullLanguageProvider(PackOutput output) {
        super(output, FullblockEnergistics.MODID, "en_us");
    }

    @Override
    public void addTranslations() {
        add("config.jade.plugin_" + FullblockEnergistics.MODID + ".storage_monitor", "Fullblock Storage Monitor");
        add("gui." + FullblockEnergistics.MODID + ".not_installed", "%s not installed.");
    }

    @NotNull
    @Override
    public String getName() {
        return "Language";
    }
}
