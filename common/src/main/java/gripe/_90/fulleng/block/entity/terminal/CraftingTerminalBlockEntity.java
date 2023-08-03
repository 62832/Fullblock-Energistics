package gripe._90.fulleng.block.entity.terminal;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.inventories.InternalInventory;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.parts.reporting.CraftingTerminalPart;
import appeng.util.inv.AppEngInternalInventory;

import gripe._90.fulleng.definition.FullEngBlockEntities;

public class CraftingTerminalBlockEntity extends StorageTerminalBlockEntity {
    private final AppEngInternalInventory craftingGrid = new AppEngInternalInventory(this, 9);

    public CraftingTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullEngBlockEntities.CRAFTING_TERMINAL, pos, blockState);
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        for (var is : craftingGrid) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        craftingGrid.readFromNBT(data, "craftingGrid");
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        craftingGrid.writeToNBT(data, "craftingGrid");
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return CraftingTermMenu.TYPE;
    }

    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        return id.equals(CraftingTerminalPart.INV_CRAFTING) ? craftingGrid : super.getSubInventory(id);
    }
}
