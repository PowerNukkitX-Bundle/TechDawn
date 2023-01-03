package cn.powernukkitx.techdawn.inventory.recipe;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.recipe.CopperBlastFurnaceBlockEntity;

public class CopperBlastFurnaceInventory extends ContainerInventory {
    public CopperBlastFurnaceInventory(CopperBlastFurnaceBlockEntity holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public CopperBlastFurnaceBlockEntity getHolder() {
        return (CopperBlastFurnaceBlockEntity) this.holder;
    }

    public Item getResult() {
        return this.getItem(2);
    }

    public Item getSmelting() {
        return this.getItem(0);
    }

    public Item getFuel() {
        return this.getItem(1);
    }

    public boolean setResult(Item item) {
        return this.setItem(2, item);
    }

    public boolean setSmelting(Item item) {
        if (!this.getSmelting().equals(item, true, false)) {
            getHolder().resetProgress();
        }
        return this.setItem(0, item);
    }

    public boolean setFuel(Item item) {
        if (!this.getFuel().equals(item, true, false)) {
            getHolder().resetProgress();
        }
        return this.setItem(1, item);
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        super.onSlotChange(index, before, send);
        this.getHolder().requestUIUpdateImmediately();
        this.getHolder().scheduleUpdate();
    }
}
