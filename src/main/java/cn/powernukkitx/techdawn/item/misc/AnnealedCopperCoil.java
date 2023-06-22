package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AnnealedCopperCoil extends BaseCoil {
    public AnnealedCopperCoil() {
        super("techdawn:annealed_copper_coil");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "annealed_copper_coil coil";
    }
}
