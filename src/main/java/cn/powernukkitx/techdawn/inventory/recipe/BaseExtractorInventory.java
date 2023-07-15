package cn.powernukkitx.techdawn.inventory.recipe;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.recipe.BaseElectricExtractorBlockEntity;

public class BaseExtractorInventory extends ContainerInventory {
    public BaseExtractorInventory(BaseElectricExtractorBlockEntity holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public BaseElectricExtractorBlockEntity getHolder() {
        return (BaseElectricExtractorBlockEntity) this.holder;
    }

    public Item getResult() {
        return this.getItem(2);
    }

    public Item getIngredient1() {
        return this.getItem(0);
    }

    public Item getIngredient2() {
        return this.getItem(1);
    }

    public boolean setResult(Item item) {
        return this.setItem(2, item);
    }

    public boolean setIngredient1(Item item) {
        if (!this.getIngredient1().equals(item, true, false)) {
            getHolder().resetProgress();
        }
        return this.setItem(0, item);
    }

    public boolean setIngredient2(Item item) {
        if (!this.getIngredient2().equals(item, true, false)) {
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
