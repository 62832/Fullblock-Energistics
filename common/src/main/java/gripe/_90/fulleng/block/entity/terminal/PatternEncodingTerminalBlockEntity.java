package gripe._90.fulleng.block.entity.terminal;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import appeng.helpers.IPatternTerminalLogicHost;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.parts.encoding.PatternEncodingLogic;

import gripe._90.fulleng.definition.FullEngBlockEntities;

public class PatternEncodingTerminalBlockEntity extends StorageTerminalBlockEntity
        implements IPatternTerminalLogicHost, IPatternTerminalMenuHost {
    private final PatternEncodingLogic logic = new PatternEncodingLogic(this);

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
    public MenuType<?> getMenuType() {
        return PatternEncodingTermMenu.TYPE;
    }

    @Override
    public PatternEncodingLogic getLogic() {
        return logic;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    @Override
    public void markForSave() {
        saveChanges();
        markForUpdate();
    }
}
