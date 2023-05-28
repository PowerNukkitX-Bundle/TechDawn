package cn.powernukkitx.techdawn.item.windmill;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.windmill.AntisepticWoodWindmillBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AntisepticWoodWindmillItem extends BaseWindmillItem {
    public AntisepticWoodWindmillItem() {
        super("techdawn:antiseptic_wood_windmill_item");
        this.block = new AntisepticWoodWindmillBlock();
    }

    @NotNull
    @Override
    public String getTags() {
        return "antiseptic_wood_windmill antiseptic_wood";
    }
}
