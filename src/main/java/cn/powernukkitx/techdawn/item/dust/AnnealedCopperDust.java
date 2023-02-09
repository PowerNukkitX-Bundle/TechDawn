package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AnnealedCopperDust extends BaseDust {
    public AnnealedCopperDust() {
        super("techdawn:annealed_copper_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "annealed_copper_dust annealed_copper copper";
    }
}
