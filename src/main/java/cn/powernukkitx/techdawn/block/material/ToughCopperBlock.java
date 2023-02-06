package cn.powernukkitx.techdawn.block.material;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class ToughCopperBlock extends BaseMaterialBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:tough_copper_block";
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER;
    }
}
