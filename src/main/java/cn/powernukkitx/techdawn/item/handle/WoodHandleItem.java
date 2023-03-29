package cn.powernukkitx.techdawn.item.handle;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.handle.WoodHandleBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class WoodHandleItem extends BaseHandleItem {
    public WoodHandleItem() {
        super("techdawn:wood_handle_item");
        this.block = new WoodHandleBlock();
    }

    @NotNull
    @Override
    public String getTags() {
        return "wood_handle wood";
    }
}
