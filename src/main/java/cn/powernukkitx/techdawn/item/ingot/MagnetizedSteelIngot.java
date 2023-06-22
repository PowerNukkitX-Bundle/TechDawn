package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class MagnetizedSteelIngot extends BaseIngot {
    public MagnetizedSteelIngot() {
        super("techdawn:magnetized_steel_ingot", "techdawn-items-ingot-magnetized_steel_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "magnetized_steel_ingot iron steel";
    }


    @Override
    public int getHardnessTier() {
        return HARDNESS_STEEL;
    }
}
