package cn.powernukkitx.techdawn.block.anvil;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class CopperAnvilBlock extends BaseAnvilBlock {
    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:copper_anvil";
    }

    public String getTextureName() {
        return "techdawn-blocks-anvil-copper_anvil";
    }

    @Override
    public int getHardnessTier() {
        return TechDawnHardness.HARDNESS_COPPER;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }
}
