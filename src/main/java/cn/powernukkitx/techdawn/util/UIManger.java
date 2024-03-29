package cn.powernukkitx.techdawn.util;

import cn.nukkit.Server;
import cn.powernukkitx.fakeInv.CustomInventory;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class UIManger {
    private final Supplier<CustomInventory> uiInventoryGenerator;
    private final UIUpdater uiInventoryUpdater;
    private final Object2IntMap<CustomInventory> displayInventories = new Object2IntOpenHashMap<>();

    public interface UIUpdater {
        void update(CustomInventory inventory, boolean immediately);
    }

    public UIManger(@NotNull Supplier<CustomInventory> uiInventoryGenerator, @NotNull UIUpdater uiInventoryUpdater) {
        this.uiInventoryGenerator = uiInventoryGenerator;
        this.uiInventoryUpdater = uiInventoryUpdater;
    }

    public CustomInventory open() {
        var inventory = uiInventoryGenerator.get();
        displayInventories.put(inventory, Server.getInstance().getTick());
        return inventory;
    }

    public void update() {
        update(false);
    }

    public void update(boolean immediately) {
        var currentTick = Server.getInstance().getTick();
        for (var iterator = displayInventories.object2IntEntrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            var each = entry.getKey();
            var createTick = entry.getIntValue();
            if (currentTick - createTick > 3 && each.getViewers().size() == 0) {
                iterator.remove();
                continue;
            }
            uiInventoryUpdater.update(each, immediately);
        }
    }

    public boolean isUIDisplaying() {
        return !displayInventories.isEmpty();
    }

}
