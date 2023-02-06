package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class TinIngot extends BaseIngot {
    public TinIngot() {
        super("techdawn:tin_ingot", "techdawn-items-ingot-tin_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "tin_ingot tin";
    }


    @Override
    public int getHardnessTier() {
        return HARDNESS_TIN;
    }
}
