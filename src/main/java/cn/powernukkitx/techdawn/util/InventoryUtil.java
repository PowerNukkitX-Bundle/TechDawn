package cn.powernukkitx.techdawn.util;

import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

public final class InventoryUtil {
    private InventoryUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取某个物品栏在某个指定槽位上发生的物品改动的即将发生的结果产物
     * @param inventory 物品栏
     * @param transaction 在某个指定槽位上发生的物品栏变更
     * @return 即将发生的结果产物
     */
    @NotNull
    public static Item getSlotTransactionResult(Inventory inventory, @NotNull InventoryTransaction transaction) {
        Item item = AIR_ITEM;
        for (var each : transaction.getActionList()) {
            if (each instanceof SlotChangeAction action) {
                if (action.getInventory() == inventory) {
                    item = action.getTargetItem();
                }
            }
        }
        return item;
    }

    /**
     * 获取某个物品栏在某个指定槽位上发生的物品改动的即将发生的结果产物
     * @param inventory 物品栏
     * @param event 在某个指定槽位上发生的物品栏变更事件
     * @return 即将发生的结果产物
     */
    @NotNull
    public static Item getSlotTransactionResult(Inventory inventory, @NotNull InventoryTransactionEvent event) {
        return getSlotTransactionResult(inventory, event.getTransaction());
    }
}
