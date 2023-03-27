package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class IronGear extends BaseGear {
    public IronGear() {
        super("techdawn:iron_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_IRON;
    }

    @NotNull
    @Override
    public String getTags() {
        return "iron_gear gear";
    }
}
