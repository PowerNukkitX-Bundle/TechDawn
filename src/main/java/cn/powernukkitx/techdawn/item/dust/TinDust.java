package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class TinDust extends BaseDust {
    public TinDust() {
        super("techdawn:tin_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TIN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "tin_dust tin";
    }
}
