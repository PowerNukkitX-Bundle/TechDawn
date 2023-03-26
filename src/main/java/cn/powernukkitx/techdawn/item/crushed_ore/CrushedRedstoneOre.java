package cn.powernukkitx.techdawn.item.crushed_ore;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CrushedRedstoneOre extends BaseCrushedOre {
    public CrushedRedstoneOre() {
        super("techdawn:crushed_redstone_ore");
    }

    @NotNull
    @Override
    public String getTags() {
        return "crushed_redstone_ore crushed_ore";
    }
}
