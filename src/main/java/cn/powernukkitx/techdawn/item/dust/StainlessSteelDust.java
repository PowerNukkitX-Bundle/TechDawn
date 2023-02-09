package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class StainlessSteelDust extends BaseDust {
    public StainlessSteelDust() {
        super("techdawn:stainless_steel_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STAINLESS_STEEL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "stainless_steel_dust stainless_steel steel iron";
    }
}
