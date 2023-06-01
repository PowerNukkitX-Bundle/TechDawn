package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;


@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AntisepticWoodBlade extends BaseBlade {
    public AntisepticWoodBlade() {
        super("techdawn:antiseptic_wood_blade");
    }

    @Override
    public int getHardnessTier() {
        return TechDawnHardness.HARDNESS_WOODEN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "antiseptic_wood_blade blade wooden antiseptic_wood";
    }
}
