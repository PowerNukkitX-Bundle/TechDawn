package cn.powernukkitx.techdawn.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.powernukkitx.fakeInv.CustomInventory;
import org.jetbrains.annotations.NotNull;

public final class InventoriesListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryTransaction(@NotNull InventoryTransactionEvent event) {
        event.getTransaction().getActions().forEach(action -> {
            if (action instanceof SlotChangeAction slotChange) {
                if (slotChange.getInventory() instanceof CustomInventory inventory) {
                    int slot = slotChange.getSlot();
                    var sourceItem = action.getSourceItem();
                    inventory.handle(slot, sourceItem, event);
                }
            }
        });
    }
}
