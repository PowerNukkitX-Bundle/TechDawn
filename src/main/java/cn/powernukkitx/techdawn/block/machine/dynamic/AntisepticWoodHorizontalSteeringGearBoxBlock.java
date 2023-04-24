package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AntisepticWoodHorizontalSteeringGearBoxBlock extends BaseHorizontalSteeringGearBoxBlock {
    @SuppressWarnings("unused")
    public AntisepticWoodHorizontalSteeringGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public AntisepticWoodHorizontalSteeringGearBoxBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:antiseptic_wood_horizontal_steering_gear_box";
    }
}
