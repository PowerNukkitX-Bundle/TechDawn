package cn.powernukkitx.techdawn.block.construct;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister(CustomBlock.class)
public class RubberWoodPlankBlock extends BlockSolid implements CustomBlock {
    @Override
    public String getName() {
        return CustomBlock.class.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:rubber_plank";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        ItemTag.registerItemTag(getNamespaceId(), List.of("minecraft:planks"));
        return CustomBlockDefinition.builder(this, "techdawn-blocks-construct-rubber_plank").build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getBurnChance() {
        return 1;
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}
