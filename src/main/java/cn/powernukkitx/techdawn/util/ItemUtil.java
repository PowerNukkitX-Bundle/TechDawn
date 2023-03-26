package cn.powernukkitx.techdawn.util;

import cn.nukkit.inventory.Fuel;
import cn.nukkit.item.Item;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public final class ItemUtil {
    private ItemUtil() {
        throw new UnsupportedOperationException();
    }

    public static void registerFuel(String stringId, int burnTime) {
        Fuel.durationByStringId.put(stringId, burnTime);
    }

    public static @NotNull Item randomItem(@NotNull Object2DoubleMap<Item> items) {
        double totalWeight = 0;
        for (double weight : items.values()) {
            totalWeight += weight;
        }
        double random = ThreadLocalRandom.current().nextDouble() * totalWeight;
        for (var entry : items.object2DoubleEntrySet()) {
            random -= entry.getDoubleValue();
            if (random <= 0) {
                return entry.getKey();
            }
        }
        return Item.AIR_ITEM;
    }
}
