package cn.powernukkitx.techdawn.item.crushed_ore;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CrushedDiamondOre extends BaseCrushedOre {
    public CrushedDiamondOre() {
        super("techdawn:crushed_diamond_ore");
    }

    @NotNull
    @Override
    public String getTags() {
        return "crushed_diamond_ore crushed_ore";
    }
}
