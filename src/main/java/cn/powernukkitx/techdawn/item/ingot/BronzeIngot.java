package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class BronzeIngot extends BaseIngot {
    public BronzeIngot() {
        super("techdawn:bronze_ingot", "techdawn-items-ingot-bronze_ingot");
    }

    @NotNull
    @Override
    public String getTags() {
        return "bronze_ingot bronze";
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_BRONZE;
    }
}
