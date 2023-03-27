package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class DiamondGear extends BaseGear {
    public DiamondGear() {
        super("techdawn:diamond_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_DIAMOND;
    }

    @NotNull
    @Override
    public String getTags() {
        return "diamond_gear gear";
    }
}
