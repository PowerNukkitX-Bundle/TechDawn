package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;

@AutoRegister(ItemCustom.class)
@AutoRegisterData("hot_tough_copper_ingot")
public class HotToughCopperIngot extends BaseIngot {
    public HotToughCopperIngot() {
        super("techdawn:hot_tough_copper_ingot", "techdawn-items-ingot-hot_tough_copper_ingot");
    }
}
