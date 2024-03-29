package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class HotCrudeAnnealedCopperIngot extends BaseIngot {
    public HotCrudeAnnealedCopperIngot() {
        super("techdawn:hot_crude_annealed_copper_ingot", "techdawn-items-ingot-hot_crude_annealed_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "hot_crude_annealed_copper_ingot hot copper";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER / 2;
    }
}
