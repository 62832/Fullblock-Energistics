package gripe._90.fulleng.integration.extendedae;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.Settings;
import appeng.api.config.ShowPatternProviders;
import appeng.api.networking.IGridNode;
import appeng.api.storage.ILinkStatus;
import appeng.api.storage.IPatternAccessTermMenuHost;
import appeng.api.util.IConfigManagerBuilder;

import gripe._90.fulleng.FullblockEnergistics;
import gripe._90.fulleng.block.entity.terminal.TerminalBlockEntity;
import gripe._90.fulleng.integration.Addons;

public class ExtendedPatternAccessTerminalBlockEntity extends TerminalBlockEntity
        implements IPatternAccessTermMenuHost {
    public ExtendedPatternAccessTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.EXTENDED_PATTERN_ACCESS_TERMINAL_BE.get(), pos, blockState);
    }

    @Override
    protected void registerSettings(IConfigManagerBuilder builder) {
        builder.registerSetting(Settings.TERMINAL_SHOW_PATTERN_PROVIDERS, ShowPatternProviders.VISIBLE);
    }

    @Nullable
    @Override
    public MenuType<?> getMenuType() {
        return Addons.EXTENDEDAE.isLoaded() ? ExtendedAEIntegration.EXTENDED_TERMINAL_MENU : null;
    }

    @Nullable
    @Override
    public IGridNode getGridNode() {
        return getMainNode().getNode();
    }

    @Override
    public ILinkStatus getLinkStatus() {
        return ILinkStatus.ofManagedNode(getMainNode());
    }
}
