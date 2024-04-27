package gripe._90.fulleng.block.entity.terminal;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.SecurityPermissions;
import appeng.helpers.IPatternTerminalLogicHost;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.parts.encoding.PatternEncodingLogic;
import appeng.util.Platform;

import gripe._90.fulleng.definition.FullEngBlockEntities;

public class PatternEncodingTerminalBlockEntity extends StorageTerminalBlockEntity implements IPatternTerminalMenuHost {
    private final IPatternTerminalLogicHost hostProxy = new LogicHostProxy();
    private final PatternEncodingLogic logic = new PatternEncodingLogic(hostProxy);

    public PatternEncodingTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBlockEntities.PATTERN_ENCODING_TERMINAL, pos, blockState);
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        logic.getBlankPatternInv().forEach(drops::add);
        logic.getEncodedPatternInv().forEach(drops::add);
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        logic.readFromNBT(data);
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        logic.writeToNBT(data);
    }

    @Override
    public MenuType<?> getMenuType(Player p) {
        return Platform.checkPermissions(p, this, SecurityPermissions.CRAFT, false, false)
                ? PatternEncodingTermMenu.TYPE
                : MEStorageMenu.TYPE;
    }

    @Override
    public PatternEncodingLogic getLogic() {
        return logic;
    }

    /**
     * Because otherwise implementing {@link IPatternTerminalLogicHost} directly means the {@code getLevel()} method
     * gets remapped to whatever {@link net.minecraft.world.level.block.entity.BlockEntity}'s own method with the same
     * mapping name is actually named.
     */
    private class LogicHostProxy implements IPatternTerminalLogicHost {
        @Override
        public PatternEncodingLogic getLogic() {
            return logic;
        }

        @Override
        public Level getLevel() {
            return level;
        }

        @Override
        public void markForSave() {
            saveChanges();
            markForUpdate();
        }
    }
}
