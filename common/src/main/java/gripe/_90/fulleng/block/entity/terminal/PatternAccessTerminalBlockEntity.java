package gripe._90.fulleng.block.entity.terminal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.Settings;
import appeng.api.config.ShowPatternProviders;

import gripe._90.fulleng.definition.FullEngBlockEntities;
import gripe._90.fulleng.menu.PatternAccessTerminalMenu;

public class PatternAccessTerminalBlockEntity extends TerminalBlockEntity {
    public PatternAccessTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBlockEntities.PATTERN_ACCESS_TERMINAL, pos, blockState);
        this.getConfigManager().registerSetting(Settings.TERMINAL_SHOW_PATTERN_PROVIDERS, ShowPatternProviders.VISIBLE);
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return PatternAccessTerminalMenu.TYPE_FULLBLOCK;
    }
}
