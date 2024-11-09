package gripe._90.fulleng.integration;

import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import gripe._90.fulleng.FullblockEnergistics;

public enum Addons {
    MEREQUESTER("ME Requester"),
    EXTENDEDAE("ExtendedAE");

    private final String modName;

    Addons(String modName) {
        this.modName = modName;
    }

    public String getModId() {
        return name().toLowerCase();
    }

    public boolean isLoaded() {
        return ModList.get() != null
                ? ModList.get().isLoaded(getModId())
                : LoadingModList.get().getMods().stream().map(ModInfo::getModId).anyMatch(getModId()::equals);
    }

    public Component getNotInstalledTooltip() {
        return Component.translatable("gui." + FullblockEnergistics.MODID + ".not_installed", modName)
                .withStyle(ChatFormatting.GRAY);
    }

    public RecipeOutput conditionalOutput(RecipeOutput output) {
        return output.withConditions(new ModLoadedCondition(getModId()));
    }
}
