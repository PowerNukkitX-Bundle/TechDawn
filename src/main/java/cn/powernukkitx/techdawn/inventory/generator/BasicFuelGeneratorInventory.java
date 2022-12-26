package cn.powernukkitx.techdawn.inventory.generator;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.generator.BasicFuelGeneratorBlockEntity;

public class BasicFuelGeneratorInventory extends ContainerInventory {
    public BasicFuelGeneratorInventory(InventoryHolder holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public BasicFuelGeneratorBlockEntity getHolder() {
        return super.getHolder() instanceof BasicFuelGeneratorBlockEntity basicFuelGeneratorBlockEntity ? basicFuelGeneratorBlockEntity : null;
    }

    public Item getFuel() {
        return this.getItem(0);
    }

    public boolean setFuel(Item item) {
        return this.setItem(0, item);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        super.onSlotChange(index, before, send);
        this.getHolder().requestUIUpdateImmediately();
        this.getHolder().scheduleUpdate();
    }
}
