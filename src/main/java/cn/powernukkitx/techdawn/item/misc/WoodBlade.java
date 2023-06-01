package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;


@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class WoodBlade extends BaseBlade {
    public WoodBlade() {
        super("techdawn:wood_blade");
    }

    @Override
    public int getHardnessTier() {
        return TechDawnHardness.HARDNESS_WOODEN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "wood_blade blade wooden";
    }
}
