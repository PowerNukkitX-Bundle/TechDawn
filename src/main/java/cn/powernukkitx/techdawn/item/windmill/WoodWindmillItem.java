package cn.powernukkitx.techdawn.item.windmill;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.windmill.WoodWindmillBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class WoodWindmillItem extends BaseWindmillItem {
    public WoodWindmillItem() {
        super("techdawn:wood_windmill_item");
        this.block = new WoodWindmillBlock();
    }

    @NotNull
    @Override
    public String getTags() {
        return "wood_windmill wood";
    }
}
