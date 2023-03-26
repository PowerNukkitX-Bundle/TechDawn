package cn.powernukkitx.techdawn.item.crushed_ore;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CrushedEmeraldOre extends BaseCrushedOre {
    public CrushedEmeraldOre() {
        super("techdawn:crushed_emerald_ore");
    }

    @NotNull
    @Override
    public String getTags() {
        return "crushed_emerald_ore crushed_ore";
    }
}
