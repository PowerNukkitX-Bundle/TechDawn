package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class BronzeGear extends BaseGear {
    public BronzeGear() {
        super("techdawn:bronze_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_BRONZE;
    }

    @NotNull
    @Override
    public String getTags() {
        return "bronze_copper_gear gear";
    }
}
