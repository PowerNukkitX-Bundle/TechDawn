package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(ItemCustom.class)
public class CrudeAnnealedCopperIngot extends BaseIngot {
    public CrudeAnnealedCopperIngot() {
        super("techdawn:crude_annealed_copper_ingot", "techdawn-items-ingot-crude_annealed_copper_ingot");
    }
}
