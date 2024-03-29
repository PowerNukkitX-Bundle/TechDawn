package cn.powernukkitx.techdawn.block.hopper;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.hopper.BaseHopperBlockEntity;
import cn.powernukkitx.techdawn.blockentity.hopper.PoweredIronHopperBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class PoweredIronHopperBlock extends BasePoweredHopperBlock {

    @SuppressWarnings("unused")
    public PoweredIronHopperBlock() {
        this(0);
    }

    public PoweredIronHopperBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:powered_iron_hopper";
    }

    @Override
    protected String getTextureName() {
        return "techdawn-blocks-hopper-iron_hopper_unpowered";
    }

    protected String getPoweredTextureName() {
        return "techdawn-blocks-hopper-iron_hopper_powered";
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 24;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @NotNull
    @Override
    public Class<? extends BaseHopperBlockEntity> getBlockEntityClass() {
        return PoweredIronHopperBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_PoweredIronHopperBlockEntity";
    }
}
