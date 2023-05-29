package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class QuartzDust extends BaseDust {
    public QuartzDust() {
        super("techdawn:quartz_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_QUARTZ;
    }

    @NotNull
    @Override
    public String getTags() {
        return "quartz_dust quartz";
    }
}
