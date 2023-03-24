package cn.powernukkitx.techdawn.item.handle;

import cn.nukkit.block.Block;
import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.level.Position;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.handle.AntisepticWoodHandleBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AntisepticWoodHandleItem extends BaseHandleItem {
    public AntisepticWoodHandleItem() {
        super("techdawn:antiseptic_wood_handle_item");
    }

    @NotNull
    @Override
    public String getTags() {
        return "antiseptic_wood_handle antiseptic_wood";
    }

    @Override
    public Block getBlock() {
        return new AntisepticWoodHandleBlock();
    }
}
