package gripe._90.fulleng.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.Settings;
import appeng.api.config.ShowPatternProviders;
import appeng.menu.implementations.PatternAccessTermMenu;

import gripe._90.fulleng.FullblockEnergistics;

public class PatternAccessTerminalBlockEntity extends TerminalBlockEntity {
    public PatternAccessTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.PATTERN_ACCESS_TERMINAL, pos, blockState);
        this.getConfigManager().registerSetting(Settings.TERMINAL_SHOW_PATTERN_PROVIDERS, ShowPatternProviders.VISIBLE);
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return PatternAccessTermMenu.TYPE;
    }
}
