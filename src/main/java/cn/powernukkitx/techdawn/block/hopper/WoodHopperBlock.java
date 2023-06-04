package cn.powernukkitx.techdawn.block.hopper;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.hopper.BaseHopperBlockEntity;
import cn.powernukkitx.techdawn.blockentity.hopper.WoodHopperBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class WoodHopperBlock extends BaseHopperBlock {

    @SuppressWarnings("unused")
    public WoodHopperBlock() {
        this(0);
    }

    public WoodHopperBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:wood_hopper";
    }

    @Override
    protected String getTextureName() {
        return "techdawn-blocks-hopper-wood_hopper";
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

    @NotNull
    @Override
    public Class<? extends BaseHopperBlockEntity> getBlockEntityClass() {
        return WoodHopperBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_WoodHopperBlockEntity";
    }
}
