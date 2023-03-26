package cn.powernukkitx.techdawn.item.crushed_ore;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CrushedCoalOre extends BaseCrushedOre {
    public CrushedCoalOre() {
        super("techdawn:crushed_coal_ore");
    }

    @NotNull
    @Override
    public String getTags() {
        return "crushed_coal_ore crushed_ore";
    }
}
