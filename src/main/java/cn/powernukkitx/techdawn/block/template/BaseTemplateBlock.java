package cn.powernukkitx.techdawn.block.template;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.anvil.BaseAnvilBlock;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class BaseTemplateBlock extends BlockTransparentMeta implements CustomBlock, TechDawnHardness, Faceable {

    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION);

    @SuppressWarnings("unused")
    public BaseTemplateBlock() {
        this(0);
    }

    public BaseTemplateBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_template";
    }

    public String getTextureName() {
        return "techdawn-blocks-template-" + getNamespaceId().substring(9);
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition
                .builder(this, Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, getTextureName()))
                .geometry("geometry.techdawn.template")
                .selectionBox(new Vector3f(-6f, 0, -6f), new Vector3f(12f, 1f, 12f))
                .collisionBox(new Vector3f(-6f, 0, -6f), new Vector3f(12f, 1f, 12f))
                .permutations(new Permutation(Component.builder().rotation(new Vector3f(0, 0, 0)).build(),
                                "q.block_property('direction') == 0"),
                        new Permutation(Component.builder().rotation(new Vector3f(0, 270, 0)).build(),
                                "q.block_property('direction') == 1"),
                        new Permutation(Component.builder().rotation(new Vector3f(0, 180, 0)).build(),
                                "q.block_property('direction') == 2"),
                        new Permutation(Component.builder().rotation(new Vector3f(0, 90, 0)).build(),
                                "q.block_property('direction') == 3"))
                .build();
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        this.setPropertyValue(CommonBlockProperties.DIRECTION, player != null ? player.getDirection().getOpposite() : BlockFace.NORTH);
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    @Override
    public boolean canBeActivated() {
        return this.down() instanceof BaseAnvilBlock;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        var down = this.down();
        if (down.canBeActivated()) {
            var result = down.onActivate(item, player);
            LevelUtil.resendAroundBlocks(this, player);
            return result;
        } else {
            return super.onActivate(item, player);
        }
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }

    @Override
    public int getLightFilter() {
        return 1;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 10.0;
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STONE;
    }

    @Override
    public boolean isProcessorBlock() {
        return true;
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(CommonBlockProperties.DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(CommonBlockProperties.DIRECTION, face);
    }

    @Override
    public double getMinX() {
        return this.x + 0.125;
    }

    @Override
    public double getMinY() {
        return this.y;
    }

    @Override
    public double getMinZ() {
        return this.z + 0.125;
    }

    @Override
    public double getMaxX() {
        return this.x + 0.875;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.0625;
    }

    @Override
    public double getMaxZ() {
        return this.z + 0.875;
    }
}
