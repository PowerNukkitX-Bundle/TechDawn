package cn.powernukkitx.techdawn.inventory.recipe;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.recipe.BaseElectricScreenerBlockEntity;

public class BaseScreenerInventory extends ContainerInventory {
    public BaseScreenerInventory(BaseElectricScreenerBlockEntity holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public BaseElectricScreenerBlockEntity getHolder() {
        return (BaseElectricScreenerBlockEntity) this.holder;
    }

    public Item getResult() {
        return this.getItem(1);
    }

    public Item getInput() {
        return this.getItem(0);
    }

    public boolean setResult(Item item) {
        return this.setItem(1, item);
    }

    public boolean setInput(Item item) {
        if (!this.getInput().equals(item, true, false)) {
            getHolder().resetProgress();
        }
        return this.setItem(0, item);
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        super.onSlotChange(index, before, send);
        this.getHolder().requestUIUpdateImmediately();
        this.getHolder().scheduleUpdate();
    }
}
