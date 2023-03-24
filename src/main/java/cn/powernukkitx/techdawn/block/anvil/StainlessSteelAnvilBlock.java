package cn.powernukkitx.techdawn.block.anvil;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class StainlessSteelAnvilBlock extends BaseAnvilBlock {
    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:stainless_steel_anvil";
    }

    public String getTextureName() {
        return "techdawn-blocks-anvil-stainless_steel_anvil";
    }

    @Override
    public int getHardnessTier() {
        return TechDawnHardness.HARDNESS_STAINLESS_STEEL;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_DIAMOND;
    }
}
