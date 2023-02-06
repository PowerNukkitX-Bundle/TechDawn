package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class HotAnnealedCopperIngot extends BaseIngot {
    public HotAnnealedCopperIngot() {
        super("techdawn:hot_annealed_copper_ingot", "techdawn-items-ingot-hot_annealed_copper_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "hot_annealed_copper_ingot copper_ingot copper";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }
}
