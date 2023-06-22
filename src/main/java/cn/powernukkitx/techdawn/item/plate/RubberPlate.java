package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class RubberPlate extends BasePlate {
    public RubberPlate() {
        super("techdawn:rubber_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_RUBBER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "rubber_plate rubber";
    }
}
