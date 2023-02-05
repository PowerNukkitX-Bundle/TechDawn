package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class StainlessSteelIngot extends BaseIngot {
    public StainlessSteelIngot() {
        super("techdawn:stainless_steel_ingot", "techdawn-items-ingot-stainless_steel_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "stainless_steel_ingot iron_ingot";
    }


    @Override
    public int getHardnessTier() {
        return HARDNESS_STAINLESS_STEEL;
    }
}
