package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class BronzeDust extends BaseDust {
    public BronzeDust() {
        super("techdawn:bronze_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_BRONZE;
    }

    @NotNull
    @Override
    public String getTags() {
        return "bronze_dust bronze";
    }
}
