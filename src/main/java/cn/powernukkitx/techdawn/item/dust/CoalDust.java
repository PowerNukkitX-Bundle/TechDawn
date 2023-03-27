package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CoalDust extends BaseDust {
    public CoalDust() {
        super("techdawn:coal_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_COAL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "coal_dust dust";
    }
}
