package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class SteelPlate extends BasePlate {
    public SteelPlate() {
        super("techdawn:steel_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STEEL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "steel_plate steel";
    }
}
