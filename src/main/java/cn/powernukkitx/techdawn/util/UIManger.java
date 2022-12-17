package cn.powernukkitx.techdawn.util;

import cn.nukkit.Server;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class UIManger {
    private final Supplier<CustomInventory> uiInventoryGenerator;
    private final Consumer<CustomInventory> uiInventoryUpdater;
    private final Object2IntMap<CustomInventory> displayInventories = new Object2IntOpenHashMap<>();

    public UIManger(@NotNull Supplier<CustomInventory> uiInventoryGenerator, @NotNull Consumer<CustomInventory> uiInventoryUpdater) {
        this.uiInventoryGenerator = uiInventoryGenerator;
        this.uiInventoryUpdater = uiInventoryUpdater;
    }

    public CustomInventory open() {
        var inventory = uiInventoryGenerator.get();
        displayInventories.put(inventory, Server.getInstance().getTick());
        return inventory;
    }

    public void update() {
        var currentTick = Server.getInstance().getTick();
        for (var iterator = displayInventories.object2IntEntrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            var each = entry.getKey();
            var createTick = entry.getIntValue();
            if (currentTick - createTick > 3 && each.getViewers().size() == 0) {
                iterator.remove();
                continue;
            }
            uiInventoryUpdater.accept(each);
        }
    }

}
