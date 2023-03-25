package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class ToughCopperPlate extends BasePlate {
    public ToughCopperPlate() {
        super("techdawn:tough_copper_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "tough_copper_plate tough_copper";
    }
}
