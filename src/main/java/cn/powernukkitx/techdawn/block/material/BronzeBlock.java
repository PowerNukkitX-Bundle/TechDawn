package cn.powernukkitx.techdawn.block.material;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BronzeBlock extends BaseMaterialBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:bronze_block";
    }

    @Override
    public double getHardness() {
        return 6;
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_BRONZE;
    }
}
