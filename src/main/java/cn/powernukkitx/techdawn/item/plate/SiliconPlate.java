package cn.powernukkitx.techdawn.item.plate;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class SiliconPlate extends BasePlate {
    public SiliconPlate() {
        super("techdawn:silicon_plate");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_SILICON;
    }

    @NotNull
    @Override
    public String getTags() {
        return "silicon_plate silicon";
    }
}
