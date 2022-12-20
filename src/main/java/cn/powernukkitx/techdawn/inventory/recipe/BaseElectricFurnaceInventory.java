package cn.powernukkitx.techdawn.inventory.recipe;

import cn.nukkit.Player;
import cn.nukkit.api.Since;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.blockentity.recipe.BaseElectricFurnaceBlockEntity;

public class BaseElectricFurnaceInventory extends ContainerInventory {
    public BaseElectricFurnaceInventory(BaseElectricFurnaceBlockEntity holder, InventoryType type) {
        super(holder, type);
    }

    @Override
    public BaseElectricFurnaceBlockEntity getHolder() {
        return (BaseElectricFurnaceBlockEntity) this.holder;
    }

    public Item getResult() {
        return this.getItem(1);
    }

    public Item getSmelting() {
        return this.getItem(0);
    }

    public boolean setResult(Item item) {
        return this.setItem(1, item);
    }

    public boolean setSmelting(Item item) {
        if (!this.getSmelting().equals(item, true, false)) {
            getHolder().resetProgress();
        }
        return this.setItem(0, item);
    }

//    @Override
//    public boolean setItemByPlayer(Player player, int index, Item item, boolean send) {
//        if (index == 2 && (item.getId() == 0 || item.getCount() == 0)) {
//            var holder = getHolder();
//            var xp = holder.calculateXpDrop();
//            if (xp > 0) {
//                holder.setStoredXP(0);
//                holder.level.dropExpOrb(player, xp);
//            }
//        }
//        return setItem(index, item, send);
//    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        super.onSlotChange(index, before, send);
        this.getHolder().requestUIUpdateImmediately();
        this.getHolder().scheduleUpdate();
    }
}
