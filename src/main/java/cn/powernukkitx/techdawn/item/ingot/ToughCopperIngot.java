package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class ToughCopperIngot extends BaseIngot {
    public ToughCopperIngot() {
        super("techdawn:tough_copper_ingot", "techdawn-items-ingot-tough_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "tough_copper_ingot tough_copper copper";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER;
    }
}
