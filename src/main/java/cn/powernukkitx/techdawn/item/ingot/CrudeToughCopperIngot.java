package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CrudeToughCopperIngot extends BaseIngot {
    public CrudeToughCopperIngot() {
        super("techdawn:crude_tough_copper_ingot", "techdawn-items-ingot-crude_tough_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "crude_tough_copper_ingot copper_ingot copper";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER / 2;
    }
}
