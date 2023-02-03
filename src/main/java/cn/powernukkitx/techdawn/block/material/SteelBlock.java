package cn.powernukkitx.techdawn.block.material;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class SteelBlock extends BaseMaterialBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:steel_block";
    }
}
