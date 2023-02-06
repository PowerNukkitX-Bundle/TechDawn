package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class IronDust extends BaseDust {
    public IronDust() {
        super("techdawn:iron_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_IRON;
    }

    @NotNull
    @Override
    public String getTags() {
        return "iron_dust iron";
    }
}
