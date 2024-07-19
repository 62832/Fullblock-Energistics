package gripe._90.fulleng.block.entity.terminal;

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
import appeng.menu.implementations.PatternAccessTermMenu;

import gripe._90.fulleng.FullblockEnergistics;

public class PatternAccessTerminalBlockEntity extends TerminalBlockEntity implements IPatternAccessTermMenuHost {
    public PatternAccessTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.PATTERN_ACCESS_TERMINAL_BE.get(), pos, blockState);
    }

    @Override
    protected void registerSettings(IConfigManagerBuilder builder) {
        builder.registerSetting(Settings.TERMINAL_SHOW_PATTERN_PROVIDERS, ShowPatternProviders.VISIBLE);
    }

    @Override
    public MenuType<?> getMenuType() {
        return PatternAccessTermMenu.TYPE;
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
