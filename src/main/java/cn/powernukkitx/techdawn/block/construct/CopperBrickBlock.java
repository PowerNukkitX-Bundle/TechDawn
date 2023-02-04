package cn.powernukkitx.techdawn.block.construct;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.item.ItemTool;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class CopperBrickBlock extends BlockSolid implements CustomBlock {
    @Override
    public String getName() {
        return CustomBlock.class.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:copper_brick";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, "techdawn-blocks-construct-copper_brick").build();
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }
}
