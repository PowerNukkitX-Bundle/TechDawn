package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(ItemCustom.class)
public class ToughCopperIngot extends BaseIngot {
    public ToughCopperIngot() {
        super("techdawn:tough_copper_ingot", "techdawn-items-ingot-tough_copper_ingot");
    }
}
