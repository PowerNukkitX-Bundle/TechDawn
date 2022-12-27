package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(ItemCustom.class)
public class AnnealedCopperIngot extends BaseIngot {
    public AnnealedCopperIngot() {
        super("techdawn:annealed_copper_ingot", "techdawn-items-ingot-annealed_copper_ingot");
    }
}
