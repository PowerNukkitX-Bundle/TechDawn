package cn.powernukkitx.techdawn.energy;

import cn.nukkit.energy.EnergyType;
import org.jetbrains.annotations.NotNull;

public final class Rotation implements EnergyType {
    private static final Rotation INSTANCE = new Rotation();

    public static Rotation getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public String getName() {
        return "rotation";
    }
}
