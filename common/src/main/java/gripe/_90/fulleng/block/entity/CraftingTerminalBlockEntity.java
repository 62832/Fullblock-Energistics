package gripe._90.fulleng.block.entity;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.SecurityPermissions;
import appeng.api.inventories.InternalInventory;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.parts.reporting.CraftingTerminalPart;
import appeng.util.Platform;
import appeng.util.inv.AppEngInternalInventory;

import gripe._90.fulleng.FullblockEnergistics;

public class CraftingTerminalBlockEntity extends StorageTerminalBlockEntity {
    private final AppEngInternalInventory craftingGrid = new AppEngInternalInventory(this, 9);

    public CraftingTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(FullblockEnergistics.CRAFTING_TERMINAL, pos, blockState);
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        for (ItemStack is : this.craftingGrid) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        this.craftingGrid.readFromNBT(data, "craftingGrid");
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        this.craftingGrid.writeToNBT(data, "craftingGrid");
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        if (Platform.checkPermissions(player, this, SecurityPermissions.CRAFT, false, false)) {
            return CraftingTermMenu.TYPE;
        }
        return MEStorageMenu.TYPE;
    }

    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        if (id.equals(CraftingTerminalPart.INV_CRAFTING)) {
            return craftingGrid;
        } else {
            return super.getSubInventory(id);
        }
    }
}
