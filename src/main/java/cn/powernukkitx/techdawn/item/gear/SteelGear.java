package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class SteelGear extends BaseGear {
    public SteelGear() {
        super("techdawn:steel_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STEEL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "steel_gear gear";
    }
}
