package cn.powernukkitx.techdawn.block.hopper;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.hopper.AntisepticWoodHopperBlockEntity;
import cn.powernukkitx.techdawn.blockentity.hopper.BaseHopperBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AntisepticWoodHopperBlock extends BaseHopperBlock {

    @SuppressWarnings("unused")
    public AntisepticWoodHopperBlock() {
        this(0);
    }

    public AntisepticWoodHopperBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:antiseptic_wood_hopper";
    }

    @Override
    protected String getTextureName() {
        return "techdawn-blocks-hopper-antiseptic_wood_hopper";
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
        return BlockColor.GRAY_BLOCK_COLOR;
    }

    @NotNull
    @Override
    public Class<? extends BaseHopperBlockEntity> getBlockEntityClass() {
        return AntisepticWoodHopperBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_AntisepticWoodHopperBlockEntity";
    }
}
