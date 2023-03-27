package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CopperGear extends BaseGear {
    public CopperGear() {
        super("techdawn:copper_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "copper_gear gear";
    }
}
