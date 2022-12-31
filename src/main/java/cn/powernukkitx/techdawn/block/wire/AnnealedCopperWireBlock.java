package cn.powernukkitx.techdawn.block.wire;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AnnealedCopperWireBlock extends BaseWireBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:annealed_copper_wire";
    }

    public String getTextureName() {
        return "techdawn-blocks-wire-annealed_copper_wire";
    }
}
