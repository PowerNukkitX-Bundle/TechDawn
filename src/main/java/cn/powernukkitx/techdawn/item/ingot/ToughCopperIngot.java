package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(ItemCustom.class)
@AutoRegisterData("tough_copper_ingot")
public class ToughCopperIngot extends BaseIngot {
    public ToughCopperIngot() {
        super("techdawn:tough_copper_ingot", "techdawn-items-ingot-tough_copper_ingot");
    }

    @Override
    @NotNull
    public String getTags() {
        return "tough_copper_ingot";
    }
}