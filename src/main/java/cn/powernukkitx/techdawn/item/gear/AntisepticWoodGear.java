package cn.powernukkitx.techdawn.item.gear;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AntisepticWoodGear extends BaseGear {
    public AntisepticWoodGear() {
        super("techdawn:antiseptic_wood_gear");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_WOODEN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "antiseptic_wood_gear gear";
    }
}
