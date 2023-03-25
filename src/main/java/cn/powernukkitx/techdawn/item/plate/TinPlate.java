package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class TinPlate extends BasePlate {
    public TinPlate() {
        super("techdawn:tin_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TIN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "tin_plate tin";
    }
}
