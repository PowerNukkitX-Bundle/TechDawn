package cn.powernukkitx.techdawn.block.hinge;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.TechDawnWorkableBlock;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.nukkit.blockproperty.CommonBlockProperties.EMPTY_PROPERTIES;
import static cn.nukkit.blockproperty.CommonBlockProperties.PILLAR_AXIS;
import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

@AutoRegister(CustomBlock.class)
public class BaseHingeBlock extends BlockTransparentMeta implements CustomBlock, TechDawnWorkableBlock {
    public static final BooleanBlockProperty TRANSPOSED = new BooleanBlockProperty("transposed", false);
    public static final BooleanBlockProperty WORKING = new BooleanBlockProperty("working", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.PILLAR_AXIS, WORKING, TRANSPOSED);

    @SuppressWarnings("unused")
    public BaseHingeBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public BaseHingeBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_hinge";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var workingMaterial = Materials.builder().any(Materials.RenderMethod.BLEND, getTextureName() + "-dyn");
        var staticMaterial = Materials.builder().any(Materials.RenderMethod.BLEND, getTextureName() + "_static");
        return CustomBlockDefinition
                .builder(this, staticMaterial)
                .geometry("geometry.techdawn.hinge")
                .selectionBox(new Vector3f(-8f, 2, -2f), new Vector3f(16f, 12f, 4f))
                .collisionBox(new Vector3f(-8f, 2, -2f), new Vector3f(16f, 12f, 4f))
                .permutations(new Permutation(Component.builder().materialInstances(staticMaterial).transformation(fromRotation(new Vector3f(0, 0, 90))).build(),
                                "q.block_property('working') == false && q.block_property('pillar_axis') == 'y' && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().materialInstances(staticMaterial).transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                                "q.block_property('working') == false && q.block_property('pillar_axis') == 'x' && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().materialInstances(staticMaterial).transformation(fromRotation(new Vector3f(0, 90, 0))).build(),
                                "q.block_property('working') == false && q.block_property('pillar_axis') == 'z' && q.block_property('transposed') == false"),

                        new Permutation(Component.builder().materialInstances(staticMaterial).transformation(fromRotation(new Vector3f(90, 0, 90))).build(),
                                "q.block_property('working') == false && q.block_property('pillar_axis') == 'y' && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().materialInstances(staticMaterial).transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                                "q.block_property('working') == false && q.block_property('pillar_axis') == 'x' && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().materialInstances(staticMaterial).transformation(fromRotation(new Vector3f(90, 90, 0))).build(),
                                "q.block_property('working') == false && q.block_property('pillar_axis') == 'z' && q.block_property('transposed') == true"),

                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(0, 0, 90))).build(),
                                "q.block_property('working') == true && q.block_property('pillar_axis') == 'y' && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                                "q.block_property('working') == true && q.block_property('pillar_axis') == 'x' && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(0, 90, 0))).build(),
                                "q.block_property('working') == true && q.block_property('pillar_axis') == 'z' && q.block_property('transposed') == false"),

                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(90, 0, 90))).build(),
                                "q.block_property('working') == true && q.block_property('pillar_axis') == 'y' && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                                "q.block_property('working') == true && q.block_property('pillar_axis') == 'x' && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(90, 90, 0))).build(),
                                "q.block_property('working') == true && q.block_property('pillar_axis') == 'z' && q.block_property('transposed') == true")
                )
                .build();
    }

    public boolean getWorkingProperty() {
        return getPropertyValue(WORKING);
    }

    public void setWorkingProperty(boolean working) {
        var same = getWorkingProperty() == working;
        setPropertyValue(WORKING, working);
        if (!same) {
            level.setBlock(this, this, true, false);
        }
    }

    public String getTextureName() {
        return "techdawn-blocks-hinge-base_hinge";
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public boolean canBeActivated() {
        return false; // When you need debug it, you can change it to true
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            if (!player.isSneaking()) {
                this.setWorkingProperty(!getWorkingProperty());
            } else {
                this.setPropertyValue(TRANSPOSED, !getPropertyValue(TRANSPOSED));
                level.setBlock(this, this, true, false);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null) {
            if (player.isSneaking()) {
                this.setPropertyValue(PILLAR_AXIS, face.getOpposite().getAxis());
            } else {
                var pitch = Math.abs(player.getPitch());
                if (pitch > 60) {
                    this.setPropertyValue(PILLAR_AXIS, BlockFace.Axis.Y);
                } else {
                    this.setPropertyValue(PILLAR_AXIS, player.getDirection().getOpposite().getAxis());
                }
            }
            // 如果相邻两边有一边是转置的，那么就设置为转置状态
            var axis = getPillarAxis();
            boolean transposed = getPropertyValue(TRANSPOSED);
            if (!transposed) {
                switch (axis) {
                    case X -> transposed = isNeighbourTransposed(1, 0, 0) || isNeighbourTransposed(-1, 0, 0);
                    case Y -> transposed = isNeighbourTransposed(0, 1, 0) || isNeighbourTransposed(0, -1, 0);
                    case Z -> transposed = isNeighbourTransposed(0, 0, 1) || isNeighbourTransposed(0, 0, -1);
                }
            }
            this.setPropertyValue(TRANSPOSED, transposed);
        }
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    private boolean isNeighbourTransposed(int dx, int dy, int dz) {
        var block = this.add(dx, dy, dz). getLevelBlock();
        var prop = block.getProperties();
        if (prop == EMPTY_PROPERTIES || !prop.contains("transposed")) return false;
        return block.getPropertyValue(TRANSPOSED);
    }

    public BlockFace.Axis getPillarAxis() {
        return getPropertyValue(PILLAR_AXIS);
    }

    public void setPillarAxis(BlockFace.Axis axis) {
        var same = getPillarAxis() == axis;
        if (!same) {
            setPropertyValue(PILLAR_AXIS, axis);
            level.setBlock(this, this, true, false);
        }
    }
}
