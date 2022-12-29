package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(ItemCustom.class)
@AutoRegisterData("hot_crude_tough_copper_ingot")
public class HotCrudeToughCopperIngot extends BaseIngot {
    public HotCrudeToughCopperIngot() {
        super("techdawn:hot_crude_tough_copper_ingot", "techdawn-items-ingot-hot_crude_tough_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "hot_crude_tough_copper_ingot";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER / 2;
    }
}
