package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;

@AutoRegister(ItemCustom.class)
@AutoRegisterData("hot_annealed_copper_ingot")
public class HotAnnealedCopperIngot extends BaseIngot {
    public HotAnnealedCopperIngot() {
        super("techdawn:hot_annealed_copper_ingot", "techdawn-items-ingot-hot_annealed_copper_ingot");
    }
}
