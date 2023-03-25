package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AnnealedCopperPlate extends BasePlate {
    public AnnealedCopperPlate() {
        super("techdawn:annealed_copper_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "annealed_copper_plate annealed_copper";
    }
}
