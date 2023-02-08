package cn.powernukkitx.techdawn.energy;

import cn.nukkit.energy.EnergyType;
import org.jetbrains.annotations.NotNull;

public class RF implements EnergyType {
    private static final RF INSTANCE = new RF();

    @NotNull
    public static RF getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public String getName() {
        return "RF";
    }

    @Override
    public double getBaseRatio() {
        return 1;
    }

    @Override
    public boolean canConvertToBase() {
        return true;
    }

    @Override
    public boolean canConvertTo(@NotNull EnergyType type) {
        return type == this || EnergyType.super.canConvertTo(type);
    }

    @Override
    public double convertTo(@NotNull EnergyType type, double amount) {
        if (type == this) return amount;
        return EnergyType.super.convertTo(type, amount);
    }
}
