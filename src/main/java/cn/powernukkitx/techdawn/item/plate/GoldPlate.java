package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class GoldPlate extends BasePlate {
    public GoldPlate() {
        super("techdawn:gold_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_GOLD;
    }

    @NotNull
    @Override
    public String getTags() {
        return "gold_plate gold";
    }
}
