package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CopperPlate extends BasePlate {
    public CopperPlate() {
        super("techdawn:copper_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "copper_plate copper";
    }
}
