package cn.powernukkitx.techdawn.inventory.actuator;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.actuator.BaseElectricDiggerBlockEntity;

public class BaseDiggerInventory extends ContainerInventory {
    public BaseDiggerInventory(BaseElectricDiggerBlockEntity holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public BaseElectricDiggerBlockEntity getHolder() {
        return (BaseElectricDiggerBlockEntity) this.holder;
    }

    public Item getPickaxe() {
        return this.getItem(0);
    }

    public boolean setPickaxe(Item item) {
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
