package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CopperDust extends BaseDust {
    public CopperDust() {
        super("techdawn:copper_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "copper_dust copper";
    }
}
