package cn.powernukkitx.techdawn.block.material;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class CrudeAnnealedCopperBlock extends BaseMaterialBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:crude_annealed_copper_block";
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_ANNEALED_COPPER;
    }
}
