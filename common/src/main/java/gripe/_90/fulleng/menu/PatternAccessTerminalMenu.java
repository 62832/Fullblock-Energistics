package gripe._90.fulleng.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.PatternAccessTermMenu;

import gripe._90.fulleng.block.entity.terminal.PatternAccessTerminalBlockEntity;

public class PatternAccessTerminalMenu extends PatternAccessTermMenu {
    public static final MenuType<PatternAccessTerminalMenu> TYPE_FULLBLOCK = MenuTypeBuilder.create(
                    PatternAccessTerminalMenu::new, PatternAccessTerminalBlockEntity.class)
            .build("patternaccessterminal_f");

    public PatternAccessTerminalMenu(int id, Inventory ip, PatternAccessTerminalBlockEntity anchor) {
        super(TYPE_FULLBLOCK, id, ip, anchor, true);
    }
}
