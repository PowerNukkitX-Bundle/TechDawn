package cn.powernukkitx.techdawn.util;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

public final class InventoryUtil {
    private static final Long2LongMap lastClickTime = new Long2LongOpenHashMap();

    private InventoryUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取某个物品栏在某个指定槽位上发生的物品改动的即将发生的结果产物
     *
     * @param inventory   物品栏
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
     * 判断某个物品栏在某个指定槽位上发生的物品改动是否安全
     *
     * @param sourceInventory  物品栏
     * @param displayInventory 在某个指定槽位上发生的物品栏变更
     * @param transaction      在某个指定槽位上发生的物品栏变更
     * @return 是否安全
     */
    public static boolean isTransactionUnsafe(@NotNull Inventory sourceInventory, @NotNull Inventory displayInventory,
                                              @NotNull InventoryTransaction transaction) {
        for (var each : transaction.getActionList()) {
            if (each instanceof SlotChangeAction action) {
                if (action.getInventory() == displayInventory) {
                    var slot = action.getSlot();
                    if (slot < 0 || slot >= sourceInventory.getSize()) {
                        return true;
                    }
                    if (!sourceInventory.getItem(slot).equalsExact(action.getSourceItem())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取某个物品栏在某个指定槽位上发生的物品改动的即将发生的结果产物
     *
     * @param inventory 物品栏
     * @param event     在某个指定槽位上发生的物品栏变更事件
     * @return 即将发生的结果产物
     */
    @NotNull
    public static Item getSlotTransactionResult(Inventory inventory, @NotNull InventoryTransactionEvent event) {
        return getSlotTransactionResult(inventory, event.getTransaction());
    }

    /**
     * 判断某个物品栏在某个指定槽位上发生的物品改动是否安全
     *
     * @param sourceInventory  物品栏
     * @param displayInventory 在某个指定槽位上发生的物品栏变更事件
     * @param event            在某个指定槽位上发生的物品栏变更事件
     * @return 是否安全
     */
    public static boolean isTransactionUnsafe(@NotNull Inventory sourceInventory, @NotNull Inventory displayInventory,
                                              @NotNull InventoryTransactionEvent event) {
        return isTransactionUnsafe(sourceInventory, displayInventory, event.getTransaction());
    }

    public static boolean ensurePlayerSafeForCustomInv(@NotNull Player player) {
        var lastClick = lastClickTime.getOrDefault(player.getLoaderId(), 0L);
        var currentClick = System.currentTimeMillis();
        if (currentClick - lastClick < 100) {
            return false;
        }
        lastClickTime.put(player.getLoaderId(), currentClick);
        return !player.isSneaking();
    }
}
