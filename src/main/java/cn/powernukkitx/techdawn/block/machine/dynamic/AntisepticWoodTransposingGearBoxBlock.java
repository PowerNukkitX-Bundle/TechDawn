package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AntisepticWoodTransposingGearBoxBlock extends BaseTransposingGearBoxBlock {
    @SuppressWarnings("unused")
    public AntisepticWoodTransposingGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public AntisepticWoodTransposingGearBoxBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:antiseptic_wood_transposing_gear_box";
    }
}
