package cn.powernukkitx.techdawn.block.hopper;

import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Geometry;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;

import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

public abstract class BasePoweredHopperBlock extends BaseHopperBlock {

    @SuppressWarnings("unused")
    public BasePoweredHopperBlock() {
        this(0);
    }

    public BasePoweredHopperBlock(int meta) {
        super(meta);
    }

    @Override
    abstract protected String getTextureName();

    abstract protected String getPoweredTextureName();

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
}
