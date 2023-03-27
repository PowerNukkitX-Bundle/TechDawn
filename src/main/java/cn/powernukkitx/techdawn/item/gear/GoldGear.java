package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class GoldGear extends BaseGear {
    public GoldGear() {
        super("techdawn:gold_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_GOLD;
    }

    @NotNull
    @Override
    public String getTags() {
        return "gold_gear gear";
    }
}
