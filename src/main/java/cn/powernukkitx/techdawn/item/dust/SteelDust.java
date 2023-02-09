package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class SteelDust extends BaseDust {
    public SteelDust() {
        super("techdawn:steel_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STEEL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "steel_dust steel iron";
    }
}
