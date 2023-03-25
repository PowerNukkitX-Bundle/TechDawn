package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class BronzePlate extends BasePlate {
    public BronzePlate() {
        super("techdawn:bronze_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_BRONZE;
    }

    @NotNull
    @Override
    public String getTags() {
        return "bronze_plate bronze";
    }
}
