package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class StainlessSteelGear extends BaseGear {
    public StainlessSteelGear() {
        super("techdawn:stainless_steel_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STAINLESS_STEEL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "stainless_steel_gear gear";
    }
}
