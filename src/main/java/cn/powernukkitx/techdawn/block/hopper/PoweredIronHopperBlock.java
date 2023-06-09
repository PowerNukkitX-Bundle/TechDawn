package cn.powernukkitx.techdawn.block.hopper;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Geometry;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.hopper.BaseHopperBlockEntity;
import cn.powernukkitx.techdawn.blockentity.hopper.PoweredIronHopperBlockEntity;
import org.jetbrains.annotations.NotNull;

import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

@AutoRegister(CustomBlock.class)
public class PoweredIronHopperBlock extends BaseHopperBlock {

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
    public boolean reversed() {
        return true;
    }

    protected BlockFace getDefaultBlockFace() {
        return BlockFace.UP;
    }

    protected BlockFace getDefaultOppositeBlockFace() {
        return BlockFace.DOWN;
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var poweredMaterial = Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, getPoweredTextureName());
        var unpoweredMaterial = Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, getTextureName());
        return CustomBlockDefinition.builder(this, unpoweredMaterial)
                .geometry("geometry.techdawn.hopper_down")
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_down"))
                        .transformation(fromRotation(new Vector3f(180, 0))).build(),
                        "q.block_property('facing_direction') == 1 && q.block_property('toggle_bit') == true"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 0))).build(),
                        "q.block_property('facing_direction') == 2 && q.block_property('toggle_bit') == true"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 180))).build(),
                        "q.block_property('facing_direction') == 3 && q.block_property('toggle_bit') == true"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 90))).build(),
                        "q.block_property('facing_direction') == 4 && q.block_property('toggle_bit') == true"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 270))).build(),
                        "q.block_property('facing_direction') == 5 && q.block_property('toggle_bit') == true"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_down"))
                        .transformation(fromRotation(new Vector3f(180, 0))).materialInstances(poweredMaterial).build(),
                        "q.block_property('facing_direction') == 1 && q.block_property('toggle_bit') == false"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 0))).materialInstances(poweredMaterial).build(),
                        "q.block_property('facing_direction') == 2 && q.block_property('toggle_bit') == false"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 180))).materialInstances(poweredMaterial).build(),
                        "q.block_property('facing_direction') == 3 && q.block_property('toggle_bit') == false"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 90))).materialInstances(poweredMaterial).build(),
                        "q.block_property('facing_direction') == 4 && q.block_property('toggle_bit') == false"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .transformation(fromRotation(new Vector3f(180, 270))).materialInstances(poweredMaterial).build(),
                        "q.block_property('facing_direction') == 5 && q.block_property('toggle_bit') == false"))
                .build();
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
        return PoweredIronHopperBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_PoweredIronHopperBlockEntity";
    }
}
