package cn.powernukkitx.techdawn.util;

import cn.nukkit.inventory.Fuel;

public final class ItemUtil {
    private ItemUtil() {
        throw new UnsupportedOperationException();
    }

    public static void registerFuel(String stringId, int burnTime) {
        Fuel.durationByStringId.put(stringId, burnTime);
    }
}
