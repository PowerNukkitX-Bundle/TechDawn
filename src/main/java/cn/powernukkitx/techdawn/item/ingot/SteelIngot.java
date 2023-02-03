package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("steel_ingot")
public class SteelIngot extends BaseIngot {
    public SteelIngot() {
        super("techdawn:steel_ingot", "techdawn-items-ingot-steel_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "steel_ingot";
    }


    @Override
    public int getHardnessTier() {
        return HARDNESS_STEEL;
    }
}
