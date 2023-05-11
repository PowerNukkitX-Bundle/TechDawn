package cn.powernukkitx.techdawn.util;

import org.jetbrains.annotations.Contract;

public final class MathUtil {
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Contract(pure = true)
    public static float sigmod(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    @Contract(pure = true)
    public static float scale(float value, float originMin, float originMax, float newMin, float newMax) {
        return (value - originMin) / (originMax - originMin) * (newMax - newMin) + newMin;
    }
}
