package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class GoldDust extends BaseDust {
    public GoldDust() {
        super("techdawn:gold_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_GOLD;
    }

    @NotNull
    @Override
    public String getTags() {
        return "gold_dust gold";
    }
}
