package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(ItemCustom.class)
@AutoRegisterData("crude_annealed_copper_ingot")
public class CrudeAnnealedCopperIngot extends BaseIngot {
    public CrudeAnnealedCopperIngot() {
        super("techdawn:crude_annealed_copper_ingot", "techdawn-items-ingot-crude_annealed_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "crude_annealed_copper_ingot";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER / 2;
    }
}
