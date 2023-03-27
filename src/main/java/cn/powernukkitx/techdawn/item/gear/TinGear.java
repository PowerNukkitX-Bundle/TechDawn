package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class TinGear extends BaseGear {
    public TinGear() {
        super("techdawn:tin_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TIN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "tin_gear gear";
    }
}
