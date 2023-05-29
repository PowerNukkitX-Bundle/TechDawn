package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.dynamic.BaseGearBoxBlockEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

@AutoRegister(CustomBlock.class)
public class BaseGearBoxBlock extends BlockSolidMeta implements CustomBlock, Faceable, BlockEntityHolder<BaseGearBoxBlockEntity> {
    public static final BooleanBlockProperty TRANSPOSED = new BooleanBlockProperty("transposed", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(TRANSPOSED, CommonBlockProperties.FACING_DIRECTION);

    @SuppressWarnings("unused")
    public BaseGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public BaseGearBoxBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_gear_box";
    }

    protected String getMainTextureName() {
        return "techdawn-blocks-gear_box-antiseptic_wood_gear_box";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition
                .builder(this, Materials.builder().north(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front")
                        .west(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_side")
                        .east(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_side")
                        .any(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_back"))
                .permutations(new Permutation(Component.builder().transformation(fromRotation(new Vector3f(270, 90, 0))).build(),
                                "q.block_property('facing_direction') == 0 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(90, 90, 0))).build(),
                                "q.block_property('facing_direction') == 1 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 180, 0))).build(),
                                "q.block_property('facing_direction') == 3 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                                "q.block_property('facing_direction') == 2 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 270, 0))).build(),
                                "q.block_property('facing_direction') == 5 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 90, 0))).build(),
                                "q.block_property('facing_direction') == 4 && q.block_property('transposed') == false"),

                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(270, 0, 0))).build(),
                                "q.block_property('facing_direction') == 0 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                                "q.block_property('facing_direction') == 1 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 180, 90))).build(),
                                "q.block_property('facing_direction') == 3 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 0, 90))).build(),
                                "q.block_property('facing_direction') == 2 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(270, 360, 90))).build(),
                                "q.block_property('facing_direction') == 5 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(90, 180, 90))).build(),
                                "q.block_property('facing_direction') == 4 && q.block_property('transposed') == true"))
                .build();
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null) {
            if (player.isSneaking()) {
                setBlockFace(face.getOpposite());
            } else {
                if (player.pitch > 60) {
                    setBlockFace(BlockFace.UP);
                } else if (player.pitch < -60) {
                    setBlockFace(BlockFace.DOWN);
                } else {
                    setBlockFace(player.getDirection().getOpposite());
                }
            }
        }
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true,
                new CompoundTag().putString("hinge_type", "techdawn:antiseptic_wood_hinge")
                        .putDouble("transfer_rate", 4.5)) != null;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    protected Sound getTransposeSound() {
        return Sound.HIT_WOOD;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        System.out.println(getBlockFace());
        if (item.isNull()) return false;
        if (player != null && !InventoryUtil.ensurePlayerSafeForCustomInv(player)) return false;
        var tags = ItemTag.getTagSet(item.getNamespaceId());
        if (tags.contains("hammer") || tags.contains("wrench")) {
            setTransposed(!isTransposed());
            var be = getBlockEntity();
            if (be != null) be.setForceUpdate(true);
            level.setBlock(this, this, true, true);
            level.addSound(this, getTransposeSound());
            return true;
        }
        return false;
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

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(CommonBlockProperties.FACING_DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(CommonBlockProperties.FACING_DIRECTION, face);
    }

    public boolean isTransposed() {
        return getPropertyValue(TRANSPOSED);
    }

    public void setTransposed(boolean transposed) {
        setPropertyValue(TRANSPOSED, transposed);
    }

    @NotNull
    @Override
    public Class<? extends BaseGearBoxBlockEntity> getBlockEntityClass() {
        return BaseGearBoxBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BaseGearBoxBlockEntity";
    }
}
