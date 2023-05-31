package cn.powernukkitx.techdawn.block.plant;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLog;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

@AutoRegister(CustomBlock.class)
public class RubberLogBlock extends BlockLog implements CustomBlock {
    public static final IntBlockProperty STATUS = new IntBlockProperty("status", false, 4);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.PILLAR_AXIS, STATUS);

    @SuppressWarnings("unused")
    public RubberLogBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public RubberLogBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:rubber_log";
    }

    public boolean isNature() {
        return (getPropertyValue(STATUS) & 0b01) != 0;
    }

    public void setNature(boolean isNature) {
        if (isNature) {
            setPropertyValue(STATUS, getPropertyValue(STATUS) | 0b01);
        } else {
            setPropertyValue(STATUS, getPropertyValue(STATUS) & 0b10);
        }
    }

    public boolean isSapping() {
        return (getPropertyValue(STATUS) & 0b10) != 0;
    }

    public void setSapping(boolean isSapping) {
        if (isSapping) {
            setPropertyValue(STATUS, getPropertyValue(STATUS) | 0b10);
        } else {
            setPropertyValue(STATUS, getPropertyValue(STATUS) & 0b01);
        }
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var normalMaterials = Materials.builder().any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_side")
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top")
                .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top");
        var sappingMaterials = Materials.builder().any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_sap")
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top")
                .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top");
        return CustomBlockDefinition.builder(this, normalMaterials)
                .permutation(new Permutation(Component.builder().materialInstances(normalMaterials)
                        .transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'y' && q.block_property('status') < 2"))
                .permutation(new Permutation(Component.builder().materialInstances(normalMaterials)
                        .transformation(fromRotation(new Vector3f(0, 90, 90))).build(),
                        "q.block_property('pillar_axis') == 'x' && q.block_property('status') < 2"))
                .permutation(new Permutation(Component.builder().materialInstances(normalMaterials)
                        .transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'z' && q.block_property('status') < 2"))
                .permutation(new Permutation(Component.builder().materialInstances(sappingMaterials)
                        .transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'y' && q.block_property('status') >= 2"))
                .permutation(new Permutation(Component.builder().materialInstances(sappingMaterials)
                        .transformation(fromRotation(new Vector3f(0, 90, 90))).build(),
                        "q.block_property('pillar_axis') == 'x' && q.block_property('status') >= 2"))
                .permutation(new Permutation(Component.builder().materialInstances(sappingMaterials)
                        .transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'z' && q.block_property('status') >= 2"))
                .customBuild(nbt -> nbt.getCompound("components").remove("minecraft:material_instances"));
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    protected BlockState getStrippedState() {
        return null;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null) {
            this.setPropertyValue(CommonBlockProperties.PILLAR_AXIS, face.getAxis());
        }
        System.out.println(getPropertyValue(CommonBlockProperties.PILLAR_AXIS));
        getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 10;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) { // Sapping
            if (isNature() && !isSapping() && ThreadLocalRandom.current().nextFloat() < 0.25f) {
                var leafExists = false;
                for (var dy = 0; dy < 8; dy++) {
                    var block = this.level.getBlock(this.getFloorX(), this.getFloorY() + dy, this.getFloorZ());
                    if (block instanceof RubberLeavesBlock) {
                        leafExists = true;
                        break;
                    } else if (block instanceof RubberLogBlock rubberLogBlock && !rubberLogBlock.isNature()) {
                        break;
                    }
                }
                if (leafExists) {
                    setSapping(true);
                    getLevel().setBlock(this, this, true, true);
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM;
            }
        }
        return Level.BLOCK_UPDATE_NORMAL;
    }
}
