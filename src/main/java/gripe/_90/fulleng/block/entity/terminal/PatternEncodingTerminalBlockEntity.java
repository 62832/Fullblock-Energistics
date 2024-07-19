package gripe._90.fulleng.block.entity.terminal;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import appeng.helpers.IPatternTerminalLogicHost;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.parts.encoding.PatternEncodingLogic;
import appeng.util.inv.AppEngInternalInventory;

import gripe._90.fulleng.FullblockEnergistics;

public class PatternEncodingTerminalBlockEntity extends StorageTerminalBlockEntity
        implements IPatternTerminalLogicHost, IPatternTerminalMenuHost {
    private final PatternEncodingLogic logic = new PatternEncodingLogic(this);

    public PatternEncodingTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.PATTERN_ENCODING_TERMINAL_BE.get(), pos, blockState);
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        logic.getBlankPatternInv().forEach(drops::add);
        logic.getEncodedPatternInv().forEach(drops::add);
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        logic.readFromNBT(data, registries);
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        logic.writeToNBT(data, registries);
    }

    @Override
    public MenuType<?> getMenuType() {
        return PatternEncodingTermMenu.TYPE;
    }

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

    @Override
    public void saveChangedInventory(AppEngInternalInventory inv) {
        saveChanges();
        markForUpdate();
    }
}
