package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AnnealedCopperGear extends BaseGear {
    public AnnealedCopperGear() {
        super("techdawn:annealed_copper_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "annealed_copper_gear gear";
    }
}
