package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class HotCrudeToughCopperIngot extends BaseIngot {
    public HotCrudeToughCopperIngot() {
        super("techdawn:hot_crude_tough_copper_ingot", "techdawn-items-ingot-hot_crude_tough_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "hot_crude_tough_copper_ingot copper_ingot copper";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER / 2;
    }
}
