package cn.powernukkitx.techdawn.inventory.recipe;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.recipe.StoneExtractorBlockEntity;

public class StoneExtractorInventory extends ContainerInventory {
    public StoneExtractorInventory(StoneExtractorBlockEntity holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public StoneExtractorBlockEntity getHolder() {
        return (StoneExtractorBlockEntity) this.holder;
    }

    public Item getResult() {
        return this.getItem(2);
    }

    public Item getInput() {
        return this.getItem(0);
    }

    public Item getExtracted() {
        return this.getItem(1);
    }

    public boolean setResult(Item item) {
        return this.setItem(2, item);
    }

    public boolean setInput(Item item) {
        if (!this.getInput().equals(item, true, false)) {
            getHolder().setStoredEnergy(0);
        }
        return this.setItem(0, item);
    }

    public boolean setExtracted(Item item) {
        if (!this.getExtracted().equals(item, true, false)) {
            getHolder().setStoredEnergy(0);
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
