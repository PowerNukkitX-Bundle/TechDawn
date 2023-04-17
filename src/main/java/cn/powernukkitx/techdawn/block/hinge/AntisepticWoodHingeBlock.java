package cn.powernukkitx.techdawn.block.hinge;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AntisepticWoodHingeBlock extends BaseHingeBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:antiseptic_wood_hinge";
    }

    @Override
    public String getTextureName() {
        return "techdawn-blocks-hinge-antiseptic_wood_hinge";
    }
}
