package gripe._90.fulleng.integration.extendedae;

import com.glodblock.github.extendedae.common.EAESingletons;
import com.glodblock.github.extendedae.container.ContainerExPatternTerminal;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public final class ExtendedAEIntegration {
    // I don't know why this is the only way it'll even work but so be it
    public static final ItemLike EXTENDED_PATTERN_TERMINAL = new ItemLike() {
        @NotNull
        @Override
        public Item asItem() {
            return EAESingletons.EX_PATTERN_TERMINAL;
        }
    };

    static final MenuType<?> EXTENDED_TERMINAL_MENU = ContainerExPatternTerminal.TYPE;
}
