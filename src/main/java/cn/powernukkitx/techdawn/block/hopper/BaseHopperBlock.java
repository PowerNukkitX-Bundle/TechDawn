package cn.powernukkitx.techdawn.block.hopper;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Geometry;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.RedstoneComponent;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.hopper.BaseHopperBlockEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.nukkit.blockproperty.CommonBlockProperties.FACING_DIRECTION;
import static cn.nukkit.blockproperty.CommonBlockProperties.TOGGLE;

@AutoRegister(CustomBlock.class)
public class BaseHopperBlock extends BlockTransparentMeta implements CustomBlock, RedstoneComponent, Faceable, BlockEntityHolder<BaseHopperBlockEntity> {
    public static final BlockProperties PROPERTIES = new BlockProperties(FACING_DIRECTION, TOGGLE);

    @SuppressWarnings("unused")
    public BaseHopperBlock() {
        this(0);
    }

    public BaseHopperBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public boolean isSolid(BlockFace side) {
        return side == BlockFace.UP;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 24;
    }

    @PowerNukkitOnly
    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    @PowerNukkitOnly
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_hopper";
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        var facing = face.getOpposite();

        if (facing == BlockFace.UP) {
            facing = BlockFace.DOWN;
        }

        setBlockFace(facing);

        if (this.level.getServer().isRedstoneEnabled()) {
            boolean powered = this.isGettingPower();

            if (powered == this.isEnabled()) {
                this.setEnabled(!powered);
            }
        }

        var nbt = new CompoundTag().putList(new ListTag<>("Items"));
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null;
    }

    protected String getTextureName() {
        return "techdawn-blocks-hopper-iron_hopper";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this,
                        Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, getTextureName()))
                .geometry("geometry.techdawn.hopper_down")
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .rotation(new Vector3f(0, 180)).build(), "q.block_property('facing_direction') == 2"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .rotation(new Vector3f(0, 0)).build(), "q.block_property('facing_direction') == 3"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .rotation(new Vector3f(0, 270)).build(), "q.block_property('facing_direction') == 4"))
                .permutation(new Permutation(Component.builder().geometry(new Geometry("geometry.techdawn.hopper_side"))
                        .rotation(new Vector3f(0, 90)).build(), "q.block_property('facing_direction') == 5"))
                .build();
    }

    @Override
    public int onUpdate(int type) {
        if (!this.level.getServer().isRedstoneEnabled()) {
            return 0;
        }

        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) {
            boolean disabled = this.level.isBlockPowered(this.getLocation());

            if (disabled == this.isEnabled()) {
                this.setEnabled(!disabled);
                this.level.setBlock(this, this, false, true);
                var be = getBlockEntity();
                if (be != null) {
                    be.setDisabled(disabled);
                    if (!disabled) {
                        be.scheduleUpdate();
                    }
                }
            }

            return type;
        }

        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            player.addWindow(getOrCreateBlockEntity().getDisplayInventory());
            if (item.canBePlaced()) LevelUtil.resendAroundBlocks(this, player);
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
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        var blockEntity = getBlockEntity();

        if (blockEntity != null) {
            return ContainerInventory.calculateRedstone(blockEntity.getInventory());
        }

        return super.getComparatorInputOverride();
    }

    @Override
    public BlockFace getBlockFace() {
        return this.getPropertyValue(CommonBlockProperties.DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(FACING_DIRECTION, face);
    }

    public boolean isEnabled() {
        return !getBooleanValue(TOGGLE);
    }

    public void setEnabled(boolean enabled) {
        setBooleanValue(TOGGLE, !enabled);
    }

    @NotNull
    @Override
    public Class<? extends BaseHopperBlockEntity> getBlockEntityClass() {
        return BaseHopperBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BaseHopperBlockEntity";
    }
}
