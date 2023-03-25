package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class IronPlate extends BasePlate {
    public IronPlate() {
        super("techdawn:iron_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_IRON;
    }

    @NotNull
    @Override
    public String getTags() {
        return "iron_plate iron";
    }
}
