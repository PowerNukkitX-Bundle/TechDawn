package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class DiamondPlate extends BasePlate {
    public DiamondPlate() {
        super("techdawn:diamond_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_DIAMOND;
    }

    @NotNull
    @Override
    public String getTags() {
        return "diamond_plate diamond";
    }
}
