package cn.powernukkitx.techdawn.block.machine.actuator;

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
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.TechDawnWorkableBlock;
import cn.powernukkitx.techdawn.blockentity.actuator.BaseElectricDiggerBlockEntity;
import cn.powernukkitx.techdawn.util.CustomDefUtil;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class BaseElectricDiggerBlock extends BlockSolidMeta implements Faceable, CustomBlock, BlockEntityHolder<BaseElectricDiggerBlockEntity>, TechDawnWorkableBlock {
    public static final BooleanBlockProperty WORKING_PROPERTY = new BooleanBlockProperty("working", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION, WORKING_PROPERTY);

    @SuppressWarnings("unused")
    public BaseElectricDiggerBlock() {
        this(0);
    }

    public BaseElectricDiggerBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_electric_digger";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomDefUtil.get4DirectionWorkingMachineDef(this,
                "techdawn-blocks-machine-basic_electric_digger_on",
                "techdawn-blocks-machine-basic_electric_digger_off",
                "techdawn-blocks-machine-basic_electric_machine_top",
                "techdawn-blocks-machine-basic_machine",
                "techdawn-blocks-machine-basic_electric_digger_bottom");
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
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            player.addWindow(getOrCreateBlockEntity().getDisplayInventory());
            getOrCreateBlockEntity().scheduleUpdate();
            if (item.canBePlaced()) LevelUtil.resendAroundBlocks(this, player);
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Class<? extends BaseElectricDiggerBlockEntity> getBlockEntityClass() {
        return BaseElectricDiggerBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BaseElectricDiggerBlock";
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        this.setPropertyValue(CommonBlockProperties.DIRECTION, player != null ? player.getDirection().getOpposite() : BlockFace.NORTH);
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }

    @Override
    public int onUpdate(int type) {
        var be = this.getBlockEntity();
        if (be != null) be.scheduleUpdate();
        return super.onUpdate(type);
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
}
