package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AnnealedCopperIngot extends BaseIngot {
    public AnnealedCopperIngot() {
        super("techdawn:annealed_copper_ingot", "techdawn-items-ingot-annealed_copper_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "annealed_copper_ingot";
    }


    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }
}
