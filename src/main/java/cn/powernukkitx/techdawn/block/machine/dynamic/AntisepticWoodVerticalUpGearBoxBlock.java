package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AntisepticWoodVerticalUpGearBoxBlock extends BaseVerticalUpSteeringGearBoxBlock {
    @SuppressWarnings("unused")
    public AntisepticWoodVerticalUpGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public AntisepticWoodVerticalUpGearBoxBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:antiseptic_wood_vertical_up_gear_box";
    }
}
