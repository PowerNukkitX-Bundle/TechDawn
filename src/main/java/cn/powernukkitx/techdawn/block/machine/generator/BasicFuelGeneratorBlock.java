package cn.powernukkitx.techdawn.block.machine.generator;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.TechDawnWorkableBlock;
import cn.powernukkitx.techdawn.blockentity.generator.BasicFuelGeneratorBlockEntity;
import cn.powernukkitx.techdawn.util.CustomDefUtil;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class BasicFuelGeneratorBlock extends BlockSolidMeta implements CustomBlock, Faceable, BlockEntityHolder<BasicFuelGeneratorBlockEntity>, TechDawnWorkableBlock {
    public static final BooleanBlockProperty WORKING_PROPERTY = new BooleanBlockProperty("working", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION, WORKING_PROPERTY);

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:basic_fuel_generator";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomDefUtil.get4DirectionWorkingBasicMachineDef(this,
                "techdawn-blocks-machine-basic_fuel_generator_on", "techdawn-blocks-machine-basic_fuel_generator_off");
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
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        this.setPropertyValue(CommonBlockProperties.DIRECTION, player != null ? player.getDirection().getOpposite() : BlockFace.NORTH);
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(CommonBlockProperties.DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(CommonBlockProperties.DIRECTION, face);
    }

    public boolean getWorkingProperty() {
        return getPropertyValue(WORKING_PROPERTY);
    }

    public void setWorkingProperty(boolean working) {
        var same = getWorkingProperty() == working;
        setPropertyValue(WORKING_PROPERTY, working);
        if (!same) {
            level.setBlock(this, this, true, true);
        }
    }

    @NotNull
    @Override
    public Class<? extends BasicFuelGeneratorBlockEntity> getBlockEntityClass() {
        return BasicFuelGeneratorBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicFuelGeneratorBlock";
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
}
